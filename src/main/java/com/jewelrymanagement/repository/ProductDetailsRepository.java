package com.jewelrymanagement.repository;

import com.jewelrymanagement.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer> {
}
