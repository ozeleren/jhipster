package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Instructor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Instructor entity.
 */
public interface InstructorSearchRepository extends ElasticsearchRepository<Instructor, Long> {
}
