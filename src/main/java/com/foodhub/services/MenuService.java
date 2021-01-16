package com.foodhub.services;

import com.foodhub.entity.MenuItem;
import com.foodhub.payload.MenuItemRequest;

import java.util.List;

/**
 * Menu Service Template
 */
public interface MenuService {

    public MenuItem addMenuItem(Long userId, MenuItemRequest menuItem);

    public List<MenuItem> fetchMenu();

    public List<MenuItem> fetchMenuByRestaurant(Long restaurantId);
}
