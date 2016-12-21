package com.bymdev.repository;

import com.bymdev.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by oleksii on 17.12.16.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
