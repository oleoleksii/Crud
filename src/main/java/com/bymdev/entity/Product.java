package com.bymdev.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by oleksii on 14.12.16.
 */

@Entity
@Table(name="T_PRODUCT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="product_id")
    private long id;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="sku")
    private String sku;

    @Column(name="name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER,cascade={CascadeType.PERSIST})
    @JoinColumn(name="category_id", nullable = false)
    private Category category;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonBackReference
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
