package com.foodhub.services;

import com.foodhub.entity.MenuItem;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.payload.MenuItemUpdateRequest;

import java.util.List;

/**
 * Menu Service Template
 */
public interface MenuService {

    public MenuItem addMenuItem(Long userId, MenuItemRequest menuItem);

    public List<MenuItem> fetchMenu();

    public List<MenuItem> fetchMenuByRestaurant(Long restaurantId);

    public boolean removeMenuItem(Long userId, Long itemId);

    public MenuItem updateMenuItem(Long userId, Long itemId, MenuItemUpdateRequest request);
}
