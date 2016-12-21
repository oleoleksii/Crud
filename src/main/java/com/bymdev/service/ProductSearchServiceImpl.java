package com.bymdev.service;

import com.bymdev.pojo.ProductSearch;
import com.bymdev.repository.ProductSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by oleksii on 17.12.16.
 */
@Service
public class ProductSearchServiceImpl implements ProductSearchService {
    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Override
    public ProductSearch save(ProductSearch productSearch) {
        productSearchRepository.save(productSearch);
        return productSearch;
    }

    @Override
    public ProductSearch findOne(String id) {
        return productSearchRepository.findOne(id);
    }

    @Override
    public Iterable<ProductSearch> findAll() {
        return productSearchRepository.findAll();
    }

    @Override
    public Page<ProductSearch> findByName(String name, Pageable pageable) {
        return productSearchRepository.findByName(name, pageable);
    }
}
