package com.jewelrymanagement.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    private String product_name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Supplier supplier_id;

    @OneToMany(mappedBy = "product_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductDetails> productDetails;

}
