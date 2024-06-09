package com.jewelrymanagement.repository;

import com.jewelrymanagement.entity.Order;
import com.jewelrymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.order_id = :orderId AND o.created_by = :createdBy")
    Optional<Order> findOrderByIdAndCreatedBy(@Param("orderId") Integer orderId, @Param("createdBy") User createdBy);

    @Query("SELECT o FROM Order o WHERE DATE(o.created_at) = CURRENT_DATE")
    List<Order> findOrdersByCurrentDate();

    @Query("SELECT o FROM Order o WHERE o.created_at >= :startDate AND o.created_at <= :endDate")
    List<Order> findOrdersByDateRange(Date startDate, Date endDate);


}
