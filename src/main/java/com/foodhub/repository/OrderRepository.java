package com.foodhub.repository;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Order;
import com.foodhub.entity.OrderStatus;
import com.foodhub.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByRestaurant(Restaurant restaurant);

    @Query("SELECT ord FROM Order ord WHERE status IN ('STATUS_RECEIVED', 'STATUS_IN_KITCHEN', 'STATUS_READY') " +
            "and restaurant = :restaurant")
    List<Order> findActiveOrdersByRestaurant(@Param("restaurant")Restaurant restaurant);

    List<Order> findByStatusEqualsOrderByRestaurant(OrderStatus status);
}
