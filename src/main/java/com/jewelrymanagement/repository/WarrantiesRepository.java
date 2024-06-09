package com.jewelrymanagement.repository;


import com.jewelrymanagement.entity.Warranties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WarrantiesRepository extends JpaRepository<Warranties, Integer> {
    @Query("SELECT o FROM Warranties o WHERE o.customer.customer_id = :customer_id")
    List<Warranties> findWarrantyByCustomerId(Integer customer_id);
    @Query("DELETE FROM Warranties o WHERE o.customer.customer_id = :customer_id")
    void deleteWarrantiesByCustomer(Integer customer_id);
}
