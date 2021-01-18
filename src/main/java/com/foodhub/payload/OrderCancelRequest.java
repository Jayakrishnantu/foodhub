package com.foodhub.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 * Payload Carrying Order Cancel Request
 */
public class OrderCancelRequest {

    @NotNull(message = "orderId cannot be empty.")
    private Long orderId;

    private Long customerId;

    public OrderCancelRequest() {
    }

    public OrderCancelRequest(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
