package com.jewelrymanagement.entity;

import com.jewelrymanagement.exceptions.WarrantyStatus;
import jakarta.persistence.*;

import java.util.Date;
@Entity
@Table(name="warranties")
public class Warranties {
    public Warranties(Integer warranty_id, Product product, Customer customer, Integer warranty_period, WarrantyStatus warranty_status, Date request_date, Date resolved_date, String description) {
        this.warranty_id = warranty_id;
        this.product = product;
        this.customer = customer;
        this.warranty_period = warranty_period;
        this.warranty_status = warranty_status;
        this.request_date = request_date;
        this.resolved_date = resolved_date;
        this.description = description;
    }

    public Warranties() {

    }

    public Integer getWarranty_id() {
        return warranty_id;
    }

    public void setWarranty_id(Integer warranty_id) {
        this.warranty_id = warranty_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Integer getWarranty_period() {
        return warranty_period;
    }

    public void setWarranty_period(Integer warranty_period) {
        this.warranty_period = warranty_period;
    }

    public WarrantyStatus getWarranty_status() {
        return warranty_status;
    }

    public void setWarranty_status(WarrantyStatus warranty_status) {
        this.warranty_status = warranty_status;
    }

    public Date getRequest_date() {
        return request_date;
    }

    public void setRequest_date(Date request_date) {
        this.request_date = request_date;
    }

    public Date getResolved_date() {
        return resolved_date;
    }

    public void setResolved_date(Date resolved_date) {
        this.resolved_date = resolved_date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer warranty_id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    private Integer warranty_period;

    @Enumerated(EnumType.STRING)
    private WarrantyStatus warranty_status;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    private Date request_date;
    private Date resolved_date;

}
