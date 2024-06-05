package com.jewelrymanagement.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productdetails")
public class ProductDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_details_id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product_id;

    private String description;

    private BigDecimal price;

    private int quantity;

    private int points;

    private String image;
}
