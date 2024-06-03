package com.jewelrymanagement.dto;

import com.jewelrymanagement.exceptions.CustomerGroup;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.math.BigDecimal;

public class CustomerDTO {
    public Integer customer_id;
    public String customer_name;
    public String phone;
    public BigDecimal total_spent;
    public Integer total_orders;
    public String address;
    public CustomerGroup customer_group;
}
