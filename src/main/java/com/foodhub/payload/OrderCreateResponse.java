package com.foodhub.payload;

import java.math.BigDecimal;

/**
 * Payload Carrying Order Create Response
 */
public class OrderCreateResponse {

    private Long orderId;
    private Integer prepTime;
    private Integer deliveryTime;
    private String message;

    private BigDecimal orderSubTotal;
    private BigDecimal orderTax;
    private BigDecimal orderDeliveryCharge;
    private BigDecimal orderTotal;

    private String restaurant;
    private String userName;

    public OrderCreateResponse() {
    }

    public OrderCreateResponse(Long orderId, Integer prepTime, Integer deliveryTime, String message,
                               BigDecimal orderSubTotal, BigDecimal orderTax, BigDecimal orderDeliveryCharge,
                               BigDecimal orderTotal, String restaurant, String userName) {
        this.orderId = orderId;
        this.prepTime = prepTime;
        this.deliveryTime = deliveryTime;
        this.message = message;
        this.orderSubTotal = orderSubTotal;
        this.orderTax = orderTax;
        this.orderDeliveryCharge = orderDeliveryCharge;
        this.orderTotal = orderTotal;
        this.restaurant = restaurant;
        this.userName = userName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getOrderSubTotal() {
        return orderSubTotal;
    }

    public void setOrderSubTotal(BigDecimal orderSubTotal) {
        this.orderSubTotal = orderSubTotal;
    }

    public BigDecimal getOrderTax() {
        return orderTax;
    }

    public void setOrderTax(BigDecimal orderTax) {
        this.orderTax = orderTax;
    }

    public BigDecimal getOrderDeliveryCharge() {
        return orderDeliveryCharge;
    }

    public void setOrderDeliveryCharge(BigDecimal orderDeliveryCharge) {
        this.orderDeliveryCharge = orderDeliveryCharge;
    }

    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
