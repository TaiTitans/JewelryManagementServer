package com.jewelrymanagement.repository;

import com.jewelrymanagement.entity.Found;
import com.jewelrymanagement.exceptions.Found.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FoundRepository extends JpaRepository<Found, Integer> {
    @Query("SELECT SUM(CASE WHEN transactionType = :transactionType THEN amount ELSE 0 END) FROM Found WHERE transactionDate = :transactionDate")
    BigDecimal DailySummary(@Param("transactionDate") LocalDate transactionDate, @Param("transactionType") TransactionType transactionType);
}
