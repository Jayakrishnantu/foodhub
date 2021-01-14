package com.foodhub.controller;

import com.foodhub.entity.MenuItem;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.services.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu/")
public class MenuController {

    Logger logger = LoggerFactory.getLogger(MenuController.class.getName());

    @Autowired
    MenuService menuService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('OWNER','CUSTOMER')")
    public List<MenuItem> fetchMenu(){
        return menuService.fetchMenu();
    }

    @PostMapping("/additem")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> addMenuItem(@RequestBody MenuItemRequest request){

        MenuItem item = menuService.addMenuItem(request);
        logger.info("Menu Item Created.");
        return new ResponseEntity<>("Menu Item Created.", HttpStatus.OK);
    }
}
