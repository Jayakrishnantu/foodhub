package com.foodhub.services;

import com.foodhub.entity.Order;
import com.foodhub.payload.OrderCancelRequest;
import com.foodhub.payload.OrderCancelResponse;
import com.foodhub.payload.OrderCreateRequest;

public interface OrderService {
    public Order createOrder(OrderCreateRequest request);

    public boolean cancelOrder(OrderCancelRequest request);
}
