package com.elastic.repository;

import com.elastic.entity.Poetry;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostRepository extends ElasticsearchRepository<Poetry, String> {
}