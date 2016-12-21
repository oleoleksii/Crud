package com.bymdev.repository;

import com.bymdev.pojo.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by oleksii on 17.12.16.
 */
@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductSearch, String> {
    Page<ProductSearch> findByName(String name, Pageable pageable);
}
