package com.jewelrymanagement.dto;

import java.math.BigDecimal;
import java.util.List;

public class OrderInvoiceDTO {
    public OrderInvoiceDTO(Integer order_id, String customer_name, String payment_method, List<OrderDetailsDTO> orderDetails, BigDecimal shipping_fee, BigDecimal total_amount) {
        this.order_id = order_id;
        this.customer_name = customer_name;
        this.payment_method = payment_method;
        this.orderDetails = orderDetails;
        this.shipping_fee = shipping_fee;
        this.total_amount = total_amount;
    }

    public OrderInvoiceDTO() {

    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public List<OrderDetailsDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailsDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public BigDecimal getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(BigDecimal shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    private Integer order_id;
    private String customer_name;
    private String payment_method;
    private List<OrderDetailsDTO> orderDetails;
    private BigDecimal shipping_fee;
    private BigDecimal total_amount;
}
