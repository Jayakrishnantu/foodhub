package com.foodhub.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Order Item Entity Object
 */
@Entity
@Table(name="orderitems")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int orderItemsId;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="orders_id")
    private Order order;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="item_id")
    private MenuItem item;

    @Column(name="quantity")
    private int qty;

    @Column(name="prep_time")
    private int prepTime;

    public OrderItem() {
    }

    public OrderItem(Order order, MenuItem item, int qty, int prepTime) {
        this.order = order;
        this.item = item;
        this.qty = qty;
        this.prepTime = prepTime;
    }

    public int getOrderItemsId() {
        return orderItemsId;
    }

    public void setOrderItemsId(int orderItemsId) {
        this.orderItemsId = orderItemsId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public MenuItem getItem() {
        return item;
    }

    public void setItem(MenuItem item) {
        this.item = item;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }
}
