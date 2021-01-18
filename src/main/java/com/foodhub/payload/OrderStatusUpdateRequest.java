package com.foodhub.payload;

import com.foodhub.entity.OrderStatus;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Payload Carrying Order Status Update Request
 */
public class OrderStatusUpdateRequest {

    @NotNull(message = "Order Id cannot be empty.")
    private Long orderId;

    @NotNull(message = "Order Status cannot be empty.")
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
