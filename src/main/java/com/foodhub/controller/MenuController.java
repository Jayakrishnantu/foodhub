package com.foodhub.controller;

import com.foodhub.entity.MenuItem;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.payload.MenuItemResponse;
import com.foodhub.security.JWTokenGenerator;
import com.foodhub.services.MenuService;
import com.foodhub.util.HubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/menu/")
public class MenuController {

    Logger logger = LoggerFactory.getLogger(MenuController.class.getName());

    @Autowired
    MenuService menuService;

    @Autowired
    HubUtil hubUtil;

    @Autowired
    private JWTokenGenerator tokenGenerator;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<MenuItemResponse> fetchMenu(){
        logger.info("fetching all items");
        return hubUtil.createMenuItemResponse(menuService.fetchMenu());
    }

    @GetMapping("/restaurant/{id}")
    @PreAuthorize("hasAnyRole('SHOP','CUSTOMER', 'ADMIN')")
    public List<MenuItemResponse> fetchMenuByRestaurant(@Valid @PathVariable("id") Long restaurantId ){
        logger.info("fetching menu for the restaurant : "+ restaurantId);
        return hubUtil.createMenuItemResponse(menuService.fetchMenuByRestaurant(restaurantId));
    }

    @PostMapping("/additem")
    @PreAuthorize("hasRole('SHOP')")
    public ResponseEntity<?> addMenuItem(@RequestBody MenuItemRequest request,
                                         @RequestHeader("Authorization") String jwToken){

        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));

        MenuItem item = menuService.addMenuItem(userId, request);
        logger.info("Menu Item Created.");
        return new ResponseEntity<>(hubUtil.readMessage("hub.menu.item.create.success"), HttpStatus.OK);
    }
}
