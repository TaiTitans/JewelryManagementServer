package com.jewelrymanagement.entity;
import jakarta.persistence.*;
import com.jewelrymanagement.exceptions.Found.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name="founds")
public class Founds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int FoundID;
    private TransactionType TransactionType;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal Amount;
    private String Description;
    private Date TransactionDate;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
}
