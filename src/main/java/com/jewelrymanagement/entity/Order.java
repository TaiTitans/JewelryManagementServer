package com.jewelrymanagement.entity;

import com.jewelrymanagement.exceptions.OrderStatus;
import com.jewelrymanagement.exceptions.PaymentMethod;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    public Order(Integer order_id, Customer customer, BigDecimal total_amount, BigDecimal shipping_fee, String notes, PaymentMethod payment_method, OrderStatus status, Date created_at, Date updated_at, String created_by) {
        this.order_id = order_id;
        this.customer = customer;
        this.total_amount = total_amount;
        this.shipping_fee = shipping_fee;
        this.notes = notes;
        this.payment_method = payment_method;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.created_by = created_by;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public BigDecimal getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(BigDecimal shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PaymentMethod getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentMethod payment_method) {
        this.payment_method = payment_method;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer order_id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    private BigDecimal total_amount;
    private BigDecimal shipping_fee;
    private String notes;

    @Enumerated(EnumType.STRING)
    private PaymentMethod payment_method;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Date created_at;


    private Date updated_at;


    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    @JoinColumn(name = "created_by")
    private String created_by;

    public Order() {

    }
}