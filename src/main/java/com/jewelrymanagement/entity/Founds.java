package com.jewelrymanagement.entity;
import jakarta.persistence.*;
import com.jewelrymanagement.exceptions.Found.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="founds")
public class Founds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int FoundID;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal Amount;
    private String Description;
    private LocalDate TransactionDate;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;

    public void setTransactionDate(LocalDate transactionDate) {
        TransactionDate = transactionDate;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        CreatedAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        UpdatedAt = updatedAt;
    }
}
