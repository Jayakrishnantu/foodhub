package com.foodhub.util;

import com.foodhub.entity.MenuItem;
import com.foodhub.payload.MenuItemRequest;

import java.math.BigDecimal;

public class HubUtil {

    public static MenuItem createMenuItem(MenuItemRequest menuItem){
        return new MenuItem(menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getPrepTime());
    }

}
