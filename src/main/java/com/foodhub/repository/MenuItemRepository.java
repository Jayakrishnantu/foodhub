package com.foodhub.repository;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for MenuItem
 */
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurant(Restaurant restaurant);

    @Query("SELECT item FROM MenuItem item WHERE LOWER(name) = LOWER(:name) AND restaurant = :restaurant")
    MenuItem findByNameAndRestaurant(String name, Restaurant restaurant);
}
