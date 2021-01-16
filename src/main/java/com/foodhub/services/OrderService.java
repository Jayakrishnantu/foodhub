package com.foodhub.services;

import com.foodhub.entity.Order;
import com.foodhub.payload.OrderCancelRequest;
import com.foodhub.payload.OrderCancelResponse;
import com.foodhub.payload.OrderCreateRequest;
import com.foodhub.payload.OrderStatusUpdateRequest;

import java.util.List;

/**
 * Order Service Template
 */
public interface OrderService {

    public Order createOrder(OrderCreateRequest request);

    public Order createOrderOnBehalf(OrderCreateRequest request, Long userId);

    public boolean cancelOrder(OrderCancelRequest request);

    public boolean statusUpdateOrder(OrderStatusUpdateRequest request);

    public List<Order> fetchOrders(Long userId);

    public List<Order> fetchActiveOrders(Long userId);

    public Order fetchOrderById(Long userId, Long orderId);

    public List<Order> fetchReadyForPickUpOrders();

    public void notifyOrderPickUp(Long orderId);

    public void notifyOrderDelivery(Long orderId);

    public Order fetchInvoiceOrder(Long userId, Long orderId);
}
