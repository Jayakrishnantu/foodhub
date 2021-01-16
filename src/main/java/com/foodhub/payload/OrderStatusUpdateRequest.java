package com.foodhub.payload;

import com.foodhub.entity.OrderStatus;

import javax.validation.constraints.NotNull;

public class OrderStatusUpdateRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private OrderStatus orderStatus;

    public OrderStatusUpdateRequest() {
    }

    public OrderStatusUpdateRequest(Long orderId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
