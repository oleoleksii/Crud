package com.bymdev.service;

import com.bymdev.entity.Product;
import com.bymdev.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by oleksii on 17.12.16.
 */
@Service
@Transactional
public class ProductServiceImpl implements GenericService<Product>{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void create(Product product) {
        productRepository.saveAndFlush(product);
    }

    @Override
    public Product read(Long id) {
        return productRepository.findOne(id);
    }

    @Override
    public List<Product> readAll() {
        return productRepository.findAll();
    }

    @Override
    public void update(Product product) {
        productRepository.saveAndFlush(product);
    }

    @Override
    public void delete(Product product) {
        product.getCategory().setProducts(null);
        productRepository.delete(product);
    }

    @Override
    public boolean isExist(Product product) {
        return productRepository.exists(product.getId());
    }
}
