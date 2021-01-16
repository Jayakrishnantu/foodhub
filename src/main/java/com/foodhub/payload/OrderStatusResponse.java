package com.foodhub.payload;

import com.foodhub.entity.OrderStatus;

/**
 * Payload for the Fetch Order Status Response
 */
public class OrderStatusResponse {

    private Long orderId;
    private String status;

    public OrderStatusResponse() {
    }

    public OrderStatusResponse(Long orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
