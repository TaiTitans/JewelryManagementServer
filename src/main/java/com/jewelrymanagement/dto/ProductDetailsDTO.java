package com.jewelrymanagement.dto;

import java.math.BigDecimal;

public class ProductDetailsDTO {
    private Integer productDetailsId;
    private Integer productId;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Integer points;
    private String image;

    // Constructors, Getters and Setters
    public ProductDetailsDTO() {
    }

    public ProductDetailsDTO(Integer productDetailsId, Integer productId, String description, BigDecimal price, Integer quantity, Integer points, String image) {
        this.productDetailsId = productDetailsId;
        this.productId = productId;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.points = points;
        this.image = image;
    }

    public Integer getProductDetailsId() {
        return productDetailsId;
    }

    public void setProductDetailsId(Integer productDetailsId) {
        this.productDetailsId = productDetailsId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
}
