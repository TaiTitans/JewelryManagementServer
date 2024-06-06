package com.jewelrymanagement.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "products")
public class Product {
    public Product(Integer product_id, String product_name, Supplier supplier_id, Set<ProductDetails> productDetails) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.supplier_id = supplier_id;
        this.productDetails = productDetails;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Supplier getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Supplier supplier_id) {
        this.supplier_id = supplier_id;
    }

    public Set<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(Set<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_id;

    private String product_name;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier_id;

    @OneToMany(mappedBy = "productId", cascade = CascadeType.ALL)
    private Set<ProductDetails> productDetails;

    public Product() {

    }
}
