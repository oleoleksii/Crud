package com.bymdev.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

/**
 * Created by oleksii on 14.12.16.
 */
@Entity
@Table(name="T_CATEGORY")
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private long id;

    @Column(name="name")
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER,cascade={CascadeType.PERSIST, CascadeType.MERGE})
    private List<Product> products;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonManagedReference
    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product){
        this.products.add(product);
        if (product.getCategory() != this){
            product.setCategory(this);
        }
    }
}
