package com.jewelrymanagement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "orderdetails")
public class OrderDetails {
    public OrderDetails(Integer order_details_id, Order order_id, Product product, Integer quantity, BigDecimal unit_price, BigDecimal discount, ProductDetails productdetails) {
        this.order_details_id = order_details_id;
        this.order_id = order_id;
        this.productdetails = productdetails;
        this.product = product;
        this.quantity = quantity;
        this.unit_price = unit_price;
        this.discount = discount;
    }

    public ProductDetails getProductdetails() {
        return productdetails;
    }
    public void setProductdetails(ProductDetails productdetails) {
        this.productdetails = productdetails;
    }

    public Integer getOrder_details_id() {
        return order_details_id;
    }

    public void setOrder_details_id(Integer order_details_id) {
        this.order_details_id = order_details_id;
    }

    public Order getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Order order_id) {
        this.order_id = order_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer order_details_id;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order_id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_details_id",nullable = false)
    private ProductDetails productdetails;


    private Integer quantity;
    private BigDecimal unit_price;
    private BigDecimal discount;

    public OrderDetails() {

    }
}
