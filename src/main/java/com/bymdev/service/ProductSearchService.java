package com.bymdev.service;

import com.bymdev.pojo.ProductSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by oleksii on 17.12.16.
 */
public interface ProductSearchService {
    ProductSearch save(ProductSearch productSearch);
    ProductSearch findOne(String id);
    Iterable<ProductSearch> findAll();
    Page<ProductSearch> findByName(String name, Pageable pageable);
}
