package com.foodhub.payload;

import com.foodhub.entity.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * Payload Carrying Generic Order Response
 */
public class OrderResponse extends DeliveryOrderResponse{

    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal deliveryCharge;

    private Integer foodPrepTimeInMinutes;
    private Integer deliveryTimeInMinutes;

    private OrderStatus orderStatus;

    private String deliveryPerson;
    private String deliveryPersonContact;

    private String instruction;

    List<String> orderItems;

    public OrderResponse() {
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(BigDecimal deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public Integer getFoodPrepTimeInMinutes() {
        return foodPrepTimeInMinutes;
    }

    public void setFoodPrepTimeInMinutes(Integer foodPrepTimeInMinutes) {
        this.foodPrepTimeInMinutes = foodPrepTimeInMinutes;
    }

    public Integer getDeliveryTimeInMinutes() {
        return deliveryTimeInMinutes;
    }

    public void setDeliveryTimeInMinutes(Integer deliveryTimeInMinutes) {
        this.deliveryTimeInMinutes = deliveryTimeInMinutes;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public List<String> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<String> orderItems) {
        this.orderItems = orderItems;
    }

    public String getDeliveryPersonContact() {
        return deliveryPersonContact;
    }

    public void setDeliveryPersonContact(String deliveryPersonContact) {
        this.deliveryPersonContact = deliveryPersonContact;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
