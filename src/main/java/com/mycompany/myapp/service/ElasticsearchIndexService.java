package com.mycompany.myapp.service;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.repository.*;
import com.mycompany.myapp.repository.search.*;
import org.elasticsearch.indices.IndexAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.ManyToMany;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    private final CourseRepository courseRepository;

    private final CourseSearchRepository courseSearchRepository;

    private final DepartmentRepository departmentRepository;

    private final DepartmentSearchRepository departmentSearchRepository;

    private final InstructorRepository instructorRepository;

    private final InstructorSearchRepository instructorSearchRepository;

    private final StudentRepository studentRepository;

    private final StudentSearchRepository studentSearchRepository;

    private final TakeRepository takeRepository;

    private final TakeSearchRepository takeSearchRepository;

    private final TeachRepository teachRepository;

    private final TeachSearchRepository teachSearchRepository;

    private final UserRepository userRepository;

    private final UserSearchRepository userSearchRepository;

    private final ElasticsearchTemplate elasticsearchTemplate;

    private static final Lock reindexLock = new ReentrantLock();

    public ElasticsearchIndexService(
        UserRepository userRepository,
        UserSearchRepository userSearchRepository,
        CourseRepository courseRepository,
        CourseSearchRepository courseSearchRepository,
        DepartmentRepository departmentRepository,
        DepartmentSearchRepository departmentSearchRepository,
        InstructorRepository instructorRepository,
        InstructorSearchRepository instructorSearchRepository,
        StudentRepository studentRepository,
        StudentSearchRepository studentSearchRepository,
        TakeRepository takeRepository,
        TakeSearchRepository takeSearchRepository,
        TeachRepository teachRepository,
        TeachSearchRepository teachSearchRepository,
        ElasticsearchTemplate elasticsearchTemplate) {
        this.userRepository = userRepository;
        this.userSearchRepository = userSearchRepository;
        this.courseRepository = courseRepository;
        this.courseSearchRepository = courseSearchRepository;
        this.departmentRepository = departmentRepository;
        this.departmentSearchRepository = departmentSearchRepository;
        this.instructorRepository = instructorRepository;
        this.instructorSearchRepository = instructorSearchRepository;
        this.studentRepository = studentRepository;
        this.studentSearchRepository = studentSearchRepository;
        this.takeRepository = takeRepository;
        this.takeSearchRepository = takeSearchRepository;
        this.teachRepository = teachRepository;
        this.teachSearchRepository = teachSearchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Async
    @Timed
    public void reindexAll() {
        if(reindexLock.tryLock()) {
            try {
                reindexForClass(Course.class, courseRepository, courseSearchRepository);
                reindexForClass(Department.class, departmentRepository, departmentSearchRepository);
                reindexForClass(Instructor.class, instructorRepository, instructorSearchRepository);
                reindexForClass(Student.class, studentRepository, studentSearchRepository);
                reindexForClass(Take.class, takeRepository, takeSearchRepository);
                reindexForClass(Teach.class, teachRepository, teachSearchRepository);
                reindexForClass(User.class, userRepository, userSearchRepository);

                log.info("Elasticsearch: Successfully performed reindexing");
            } finally {
                reindexLock.unlock();
            }
        } else {
            log.info("Elasticsearch: concurrent reindexing attempt");
        }
    }

    @SuppressWarnings("unchecked")
    private <T, ID extends Serializable> void reindexForClass(Class<T> entityClass, JpaRepository<T, ID> jpaRepository,
                                                              ElasticsearchRepository<T, ID> elasticsearchRepository) {
        elasticsearchTemplate.deleteIndex(entityClass);
        try {
            elasticsearchTemplate.createIndex(entityClass);
        } catch (IndexAlreadyExistsException e) {
            // Do nothing. Index was already concurrently recreated by some other service.
        }
        elasticsearchTemplate.putMapping(entityClass);
        if (jpaRepository.count() > 0) {
            // if a JHipster entity field is the owner side of a many-to-many relationship, it should be loaded manually
            List<Method> relationshipGetters = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getType().equals(Set.class))
                .filter(field -> field.getAnnotation(ManyToMany.class) != null)
                .filter(field -> field.getAnnotation(ManyToMany.class).mappedBy().isEmpty())
                .filter(field -> field.getAnnotation(JsonIgnore.class) == null)
                .map(field -> {
                    try {
                        return new PropertyDescriptor(field.getName(), entityClass).getReadMethod();
                    } catch (IntrospectionException e) {
                        log.error("Error retrieving getter for class {}, field {}. Field will NOT be indexed",
                            entityClass.getSimpleName(), field.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            int size = 100;
            for (int i = 0; i <= jpaRepository.count() / size; i++) {
                Pageable page = new PageRequest(i, size);
                log.info("Indexing page {} of {}, size {}", i, jpaRepository.count() / size, size);
                Page<T> results = jpaRepository.findAll(page);
                results.map(result -> {
                    // if there are any relationships to load, do it now
                    relationshipGetters.forEach(method -> {
                        try {
                            // eagerly load the relationship set
                            ((Set) method.invoke(result)).size();
                        } catch (Exception ex) {
                            log.error(ex.getMessage());
                        }
                    });
                    return result;
                });
                elasticsearchRepository.save(results.getContent());
            }
        }
        log.info("Elasticsearch: Indexed all rows for {}", entityClass.getSimpleName());
    }
}
