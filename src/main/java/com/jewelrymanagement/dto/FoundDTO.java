package com.jewelrymanagement.dto;

import com.jewelrymanagement.exceptions.Found.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FoundDTO {
    private Integer foundID;
    private TransactionType transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private String created_by;

    // Getters and Setters
    public int getFoundID() {
        return foundID;
    }

    public void setFoundID(int foundID) {
        this.foundID = foundID;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }


    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
