package com.foodhub.services;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Restaurant;
import com.foodhub.entity.User;
import com.foodhub.exception.MenuFetchException;
import com.foodhub.exception.MenuItemCreateException;
import com.foodhub.exception.MenuItemException;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.payload.MenuItemUpdateRequest;
import com.foodhub.repository.MenuItemRepository;
import com.foodhub.repository.RestaurantRepository;
import com.foodhub.repository.UserRepository;
import com.foodhub.util.HubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Service Implementation for Menu Service
 */
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

        MenuItem item = menuItemRepository
                .findByNameAndRestaurant(menuItem.getName(), restaurant);
        if(null != item) {
           throw new MenuItemCreateException(
                    hubUtil.readMessage("hub.menu.item.create.already.exists"));
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

    @Override
    @Transactional
    public boolean removeMenuItem(Long userId, Long itemId) {
        MenuItem item = menuItemRepository
                .findById(itemId)
                .orElseThrow(()->
                        new MenuItemException(hubUtil.readMessage("hub.menu.item.not.exists"))
        );
        User user = userRepository.findById(userId).orElse(null);
        if(null == user || !user.getRestaurantId().equals(item.getRestaurant().getRestaurantId())){
            throw new MenuItemException(hubUtil.readMessage("hub.menu.item.delete.not.auth"));
        }
        // Item Marked for deletion
        item.setStatus(0);
        menuItemRepository.save(item);
        return true;
    }

    @Override
    @Transactional
    public MenuItem updateMenuItem(Long userId, Long itemId, MenuItemUpdateRequest request) {

        MenuItem item = menuItemRepository
                .findById(itemId)
                .orElseThrow(() ->
                        new MenuItemException(hubUtil.readMessage("hub.menu.item.not.exists"))
                );
        User user = userRepository.findById(userId).orElse(null);
        if (null == user || !user.getRestaurantId().equals(item.getRestaurant().getRestaurantId())) {
            throw new MenuItemException(hubUtil.readMessage("hub.menu.item.update.not.auth"));
        }

        if (StringUtils.hasText(request.getName())) item.setName(request.getName());
        if (StringUtils.hasText(request.getDescription())) item.setDescription(request.getDescription());
        if (null != request.getPrice()) item.setPrice(request.getPrice());
        if (null != request.getPrepTime()) item.setPrepTime(request.getPrepTime());

        menuItemRepository.save(item);

        return item;
    }
}
