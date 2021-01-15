package com.foodhub.repository;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurant(Restaurant restaurant);
}
