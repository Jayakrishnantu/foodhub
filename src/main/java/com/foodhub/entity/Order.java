package com.foodhub.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

/**
 * Order Entity Object
 */
@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long orderId;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "customer_id")
    private User user;

    @Column(name="address")
    private String address;

    @Column(name="item_total")
    private BigDecimal itemTotal;

    @Column(name="tax")
    private BigDecimal tax;

    @Column(name="delivery_charge")
    private BigDecimal deliveryCharge;

    @Column(name="total")
    private BigDecimal total;

    @Column(name="prep_time")
    private int prepTime;

    @Column(name="delivery_time")
    private int deliveryTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "delivery_by")
    private User deliveryBy;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    List<OrderItem> orderItemList;

    public Order() {
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getDeliveryBy() {
        return deliveryBy;
    }

    public void setDeliveryBy(User deliveryBy) {
        this.deliveryBy = deliveryBy;
    }

    public List<OrderItem> getOrderItemsList() {
        return orderItemList;
    }

    public void setOrderItemsList(List<OrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", user=" + user +
                ", address='" + address + '\'' +
                ", itemTotal=" + itemTotal +
                ", tax=" + tax +
                ", deliveryCharge=" + deliveryCharge +
                ", total=" + total +
                ", prepTime=" + prepTime +
                ", deliveryTime=" + deliveryTime +
                ", status=" + status +
                ", deliveryBy=" + deliveryBy +
                ", restaurant=" + restaurant +
                ", orderItemList=" + orderItemList +
                '}';
    }
}
