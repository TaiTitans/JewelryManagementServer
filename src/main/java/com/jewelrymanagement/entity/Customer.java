package com.jewelrymanagement.entity;

import jakarta.persistence.*;
import com.jewelrymanagement.exceptions.CustomerGroup;
import java.math.BigDecimal;


@Entity
@Table(name = "customers")
public class Customer {
    public Customer(Integer customer_id, String customer_name, String phone, BigDecimal total_points, BigDecimal total_orders, String address, CustomerGroup customer_group) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
        this.phone = phone;
        this.total_points = total_points;
        this.total_orders = total_orders;
        this.address = address;
        this.customer_group = customer_group;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getTotal_points() {
        return total_points;
    }

    public void setTotal_points(BigDecimal total_points) {
        this.total_points = total_points;
    }

    public BigDecimal getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(BigDecimal total_orders) {
        this.total_orders = total_orders;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CustomerGroup getCustomer_group() {
        return customer_group;
    }

    public void setCustomer_group(CustomerGroup customer_group) {
        this.customer_group = customer_group;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customer_id;
    private String customer_name;
    private String phone;
    private BigDecimal total_points;
    private BigDecimal total_orders;
    private String address;
    @Enumerated(EnumType.STRING)
    private CustomerGroup customer_group;

    public Customer() {

    }
}
