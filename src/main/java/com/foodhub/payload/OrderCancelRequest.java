package com.foodhub.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class OrderCancelRequest {

    @NotNull
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
