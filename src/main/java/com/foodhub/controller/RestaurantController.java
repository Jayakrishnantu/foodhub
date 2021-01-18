package com.foodhub.controller;

import com.foodhub.entity.Restaurant;
import com.foodhub.payload.RestaurantRequest;
import com.foodhub.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {

    @Autowired
    RestaurantService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRestaurant(@Valid @RequestBody RestaurantRequest request){
        return new ResponseEntity<>(service.createRestaurant(request), HttpStatus.OK);
    }
}
