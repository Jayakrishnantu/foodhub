package com.foodhub.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum for holding the Order Status
 */
public enum OrderStatus {
    STATUS_RECEIVED(1, "Order Received"),
    STATUS_IN_KITCHEN(2, "In Kitchen"),
    STATUS_READY(3, "Ready for Pickup"),
    STATUS_PICKED_UP(4, "Out for Delivery"),
    STATUS_DELIVERED(5, "Delivered"),
    STATUS_CANCELLED(6, "Cancelled");

    private Integer value;
    private String description;

    private static Map<Integer, OrderStatus> statusMapping;

    private OrderStatus(Integer value, String description){
        this.value = value;
        this.description = description;
    }

    public static OrderStatus getStatus(Integer value){
        if(statusMapping == null){
            initMapping();
        }
        return statusMapping.get(value);
    }

    private static void initMapping(){
        statusMapping = new HashMap<>();
        for(OrderStatus s : values()){
            statusMapping.put(s.value, s);
        }
    }

    public Integer getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
