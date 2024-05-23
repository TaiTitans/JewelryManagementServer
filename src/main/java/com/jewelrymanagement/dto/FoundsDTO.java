package com.jewelrymanagement.dto;

import com.jewelrymanagement.exceptions.Found.TransactionType;
import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Date;

public class FoundsDTO {
    public int FoundID;
    public com.jewelrymanagement.exceptions.Found.TransactionType transactionType;
    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal Amount;
    public String Description;
    public LocalDate TransactionDate;
    public LocalDateTime CreatedAt;
    public LocalDateTime UpdatedAt;
}
