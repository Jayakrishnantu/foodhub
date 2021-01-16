package com.foodhub.payload;

/**
 * Payload Carrying Order Status Update Response
 */
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
