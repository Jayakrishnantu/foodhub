package com.foodhub.services;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Restaurant;
import com.foodhub.exception.MenuFetchException;
import com.foodhub.exception.MenuItemCreateException;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.repository.MenuItemRepository;
import com.foodhub.repository.RestaurantRepository;
import com.foodhub.util.HubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class HubMenuService implements MenuService{

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    @Transactional
    public MenuItem addMenuItem(MenuItemRequest menuItem) {

        Restaurant restaurant = restaurantRepository.findById(menuItem.getRestaurantId()).orElse(null);

        if(null == restaurant){
            throw new MenuItemCreateException("Invalid Restaurant : id # "+ menuItem.getRestaurantId());
        }
        MenuItem entity = HubUtil.createMenuItem(menuItem, restaurant);
        menuItemRepository.save(entity);
        return entity;
    }

    @Override
    public List<MenuItem> fetchMenu() {
        return menuItemRepository.findAll();
    }

    @Override
    public List<MenuItem> fetchMenuByRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);

        if(restaurant == null){
            throw new MenuFetchException("Invalid Restaurant");
        }

        return menuItemRepository.findByRestaurant(restaurant);
    }
}
