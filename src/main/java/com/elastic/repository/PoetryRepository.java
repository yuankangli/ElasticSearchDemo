package com.elastic.repository;

import com.elastic.entity.Poetry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PoetryRepository extends ElasticsearchRepository<Poetry, String> {
}