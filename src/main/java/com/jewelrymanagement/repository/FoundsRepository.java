package com.jewelrymanagement.repository;

import com.jewelrymanagement.entity.Founds;
import com.jewelrymanagement.exceptions.Found.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface FoundsRepository extends JpaRepository<Founds, Integer> {
    @Query("SELECT SUM(CASE WHEN transactionType = :transactionType THEN Amount ELSE 0 END) FROM Founds WHERE TransactionDate = :transactionDate")
    BigDecimal DailySummary(@Param("transactionDate") LocalDate transactionDate, @Param("transactionType") TransactionType transactionType);
}
