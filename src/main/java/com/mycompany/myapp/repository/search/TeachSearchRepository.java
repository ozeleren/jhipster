package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Teach;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Teach entity.
 */
public interface TeachSearchRepository extends ElasticsearchRepository<Teach, Long> {
}
