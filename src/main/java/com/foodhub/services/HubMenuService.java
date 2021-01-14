package com.foodhub.services;

import com.foodhub.entity.MenuItem;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.repository.MenuItemRepository;
import com.foodhub.util.HubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class HubMenuService implements MenuService{

    @Autowired
    MenuItemRepository menuItemRepository;

    @Override
    @Transactional
    public MenuItem addMenuItem(MenuItemRequest menuItem) {

        MenuItem entity = HubUtil.createMenuItem(menuItem);
        menuItemRepository.save(entity);
        return entity;
    }

    @Override
    public List<MenuItem> fetchMenu() {
        return menuItemRepository.findAll();
    }
}
