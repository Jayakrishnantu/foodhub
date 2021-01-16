package com.foodhub.payload;

public class OrderStatusUpdateResponse {

    private long orderId;
    private String message;

    public OrderStatusUpdateResponse() {
    }

    public OrderStatusUpdateResponse(long orderId, String message) {
        this.orderId = orderId;
        this.message = message;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
