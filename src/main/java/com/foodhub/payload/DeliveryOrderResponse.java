package com.foodhub.payload;

import java.math.BigDecimal;

public class DeliveryOrderResponse {
    private Long orderId;
    private String userName;
    private String deliverAddress;
    private String customerContact;

    private BigDecimal total;

    private String restaurantName;
    private String restaurantAddress;
    private String restaurantContactInfo;

    public DeliveryOrderResponse() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public String getCustomerContact() {
        return customerContact;
    }

    public void setCustomerContact(String customerContact) {
        this.customerContact = customerContact;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantContactInfo() {
        return restaurantContactInfo;
    }

    public void setRestaurantContactInfo(String restaurantContactInfo) {
        this.restaurantContactInfo = restaurantContactInfo;
    }
}
