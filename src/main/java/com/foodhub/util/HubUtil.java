package com.foodhub.util;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Restaurant;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.payload.MenuItemResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HubUtil {

    public static MenuItem createMenuItem(MenuItemRequest menuItem, Restaurant restaurant){
        return new MenuItem(menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getPrepTime(),
                restaurant);
    }

    public static List<MenuItemResponse> createMenuItemResponse(List<MenuItem> menuItems){
        List<MenuItemResponse> responses = new ArrayList<>();
        for(MenuItem item: menuItems){
            MenuItemResponse response = new MenuItemResponse(item.getId(), item.getName(),
                    item.getDescription(), item.getPrice(), item.getPrepTime(),
                    item.getRestaurant().getRestaurantId(), item.getRestaurant().getRestaurantName());
            responses.add(response);
        }
        return responses;
    }

    public static String getToken(String token){
        return token.substring(7);
    }

    public static Integer findPrepTime(Integer qty, Integer prepTime){
        return Math.multiplyExact(qty, prepTime);
    }

    public static BigDecimal findItemTotal(Integer qty, BigDecimal unitPrice){
        return unitPrice.multiply(BigDecimal.valueOf(qty.longValue()));
    }
}
