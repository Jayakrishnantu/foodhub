package com.foodhub.services;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Restaurant;
import com.foodhub.entity.User;
import com.foodhub.exception.MenuFetchException;
import com.foodhub.exception.MenuItemCreateException;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.repository.MenuItemRepository;
import com.foodhub.repository.RestaurantRepository;
import com.foodhub.repository.UserRepository;
import com.foodhub.util.HubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class HubMenuService implements MenuService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    HubUtil hubUtil;

    @Override
    @Transactional
    public MenuItem addMenuItem(Long userId, MenuItemRequest menuItem) {

        Restaurant restaurant = restaurantRepository
                .findById(menuItem.getRestaurantId())
                .orElseThrow(() -> new MenuItemCreateException(
                        hubUtil.readMessage("hub.menu.item.invalid.restaurant")+ menuItem.getRestaurantId())
        );

        User user = userRepository.findById(userId).orElse(null);
        if(null == user || !user.getRestaurantId().equals(menuItem.getRestaurantId())){
            throw new MenuItemCreateException(hubUtil.readMessage("hub.menu.item.create.not.auth"));
        }
        MenuItem entity = hubUtil.createMenuItem(menuItem, restaurant);
        menuItemRepository.save(entity);
        return entity;
    }

    @Override
    @Transactional
    public List<MenuItem> fetchMenu() {
        return menuItemRepository.findAll();
    }

    @Override
    @Transactional
    public List<MenuItem> fetchMenuByRestaurant(Long restaurantId) {

        Restaurant restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(()->new MenuFetchException(
                        hubUtil.readMessage("hub.menu.item.invalid.restaurant")+ restaurantId)
        );
        return menuItemRepository.findByRestaurant(restaurant);
    }
}
