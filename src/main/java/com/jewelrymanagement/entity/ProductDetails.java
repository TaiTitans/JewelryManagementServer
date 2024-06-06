package com.jewelrymanagement.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productdetails")
public class ProductDetails {


    public ProductDetails(Integer product_details_id, Product productId, String description, BigDecimal price, Integer quantity, Integer points, String image) {
        this.product_details_id = product_details_id;
        this.productId = productId;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.points = points;
        this.image = image;
    }

    public Product getProductId() {
        return productId;
    }

    public void setProductId(Product productId) {
        this.productId = productId;
    }

    public Integer getProduct_details_id() {
        return product_details_id;
    }

    public void setProduct_details_id(Integer product_details_id) {
        this.product_details_id = product_details_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer product_details_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product productId;

    private String description;

    private BigDecimal price;

    private Integer quantity;

    private Integer points;

    private String image;

    public ProductDetails() {

    }
}
