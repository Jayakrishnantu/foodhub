package com.foodhub.services;

import com.foodhub.entity.Restaurant;
import com.foodhub.payload.RestaurantRequest;

public interface RestaurantService {

    public Restaurant createRestaurant(RestaurantRequest request);
}
