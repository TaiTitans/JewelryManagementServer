package com.jewelrymanagement.model;

import java.math.BigDecimal;

public class MonthlyReport {
    private int totalOrders;
    private BigDecimal totalAmount;

    public MonthlyReport(int totalOrders, BigDecimal totalAmount) {
        this.totalOrders = totalOrders;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
