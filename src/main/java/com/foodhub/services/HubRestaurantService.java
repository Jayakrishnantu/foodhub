package com.foodhub.services;

import com.foodhub.entity.Restaurant;
import com.foodhub.payload.RestaurantRequest;
import com.foodhub.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HubRestaurantService implements RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Override
    public Restaurant createRestaurant(RestaurantRequest request) {

        Restaurant restaurant = new Restaurant();
        restaurant.setRestaurantName(request.getName());
        restaurant.setRestaurantAddress(request.getAddress());
        restaurant.setRestaurantContactInfo(request.getContact());

        restaurantRepository.save(restaurant);

        return restaurant;
    }
}
