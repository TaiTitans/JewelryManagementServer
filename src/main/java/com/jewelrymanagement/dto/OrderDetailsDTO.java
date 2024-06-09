package com.jewelrymanagement.dto;

import com.jewelrymanagement.entity.Order;
import com.jewelrymanagement.entity.ProductDetails;

import java.math.BigDecimal;

public class OrderDetailsDTO {
    public Integer order_details_id;
    public Integer order_id;
    public Integer product_id;
    public Integer product_details_id;
    public Integer quantity;
    public BigDecimal unit_price;
    public BigDecimal discount;
}
