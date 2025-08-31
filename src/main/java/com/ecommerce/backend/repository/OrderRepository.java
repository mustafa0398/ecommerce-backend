package com.ecommerce.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecommerce.backend.model.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);

    long countByStatus(String status);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.createdAt >= :since")
    Double sumRevenueSince(@Param("since") long since);

    @Query("SELECT oi.title, SUM(oi.quantity) as qty " +
           "FROM OrderItem oi JOIN oi.order o " +
           "WHERE o.createdAt >= :since " +
           "GROUP BY oi.title " +
           "ORDER BY qty DESC")
    List<Object[]> findTopProductsSince(@Param("since") long since);
}
