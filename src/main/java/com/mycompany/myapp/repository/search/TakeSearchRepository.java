package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Take;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Take entity.
 */
public interface TakeSearchRepository extends ElasticsearchRepository<Take, Long> {
}
