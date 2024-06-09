package com.jewelrymanagement.repository;

import com.jewelrymanagement.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Integer> {
    @Query("SELECT pd FROM ProductDetails pd WHERE pd.productId.product_id = :productId")
    List<ProductDetails> findProductByProductId(Integer productId);

    @Query("DELETE FROM ProductDetails pd WHERE pd.productId.product_id = :productId")
    void deleteAllByProductId(Integer productId);

    @Query("SELECT pd FROM ProductDetails pd WHERE pd.productId = :productId")
    Optional<ProductDetails> findByProductId(Integer productId);
}
