package com.foodhub.repository;

import com.foodhub.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Order Item
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
