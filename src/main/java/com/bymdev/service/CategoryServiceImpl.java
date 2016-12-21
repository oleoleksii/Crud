package com.bymdev.service;

import com.bymdev.entity.Category;
import com.bymdev.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by oleksii on 17.12.16.
 */
@Service
@Transactional
public class CategoryServiceImpl implements GenericService<Category> {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void create(Category category) {
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public Category read(Long id) {
        return categoryRepository.findOne(id);
    }

    @Override
    public List<Category> readAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void update(Category category) {
        categoryRepository.saveAndFlush(category);
    }

    @Override
    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    @Override
    public boolean isExist(Category category) {
        return categoryRepository.exists(category.getId());
    }

}
