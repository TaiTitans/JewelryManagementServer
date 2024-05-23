package com.jewelrymanagement.dto;

import com.jewelrymanagement.exceptions.Found.TransactionType;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class FoundsDTO {
    public int FoundID;
    public com.jewelrymanagement.exceptions.Found.TransactionType TransactionType;
    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal Amount;
    public String Description;
    public Date TransactionDate;
    public LocalDateTime CreatedAt;
    public LocalDateTime UpdatedAt;
}
