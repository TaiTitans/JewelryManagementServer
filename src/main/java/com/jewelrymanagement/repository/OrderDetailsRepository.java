package com.jewelrymanagement.repository;

import com.jewelrymanagement.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Integer> {
    @Query("SELECT o FROM OrderDetails o WHERE o.order_id.order_id = :order_id")
    List<OrderDetails> findOrderDetailsByOrderId(Integer order_id);

    @Query("DELETE FROM OrderDetails o WHERE o.order_id.order_id = :order_id")
    void deleteOrderDetailsByOrderId(Integer order_id);


    @Query("SELECT od FROM OrderDetails od WHERE od.product.product_id = :productId AND od.product_details_id = :productDetailsId")
    Optional<OrderDetails> findByProductIdAndProductDetailsId(Integer productId, Integer productDetailsId);


}
