package com.jewelrymanagement.dto;

import com.jewelrymanagement.exceptions.OrderStatus;
import com.jewelrymanagement.exceptions.PaymentMethod;

import java.math.BigDecimal;
import java.util.Date;

public class OrderDTO {
    public Integer order_id;
    public Integer customer_id;
    public BigDecimal total_amount;
    public BigDecimal shipping_fee;
    public String notes;
    public PaymentMethod payment_method;
    public OrderStatus status;
    public Date created_at;
    public Date updated_at;
    public String created_by;
}
