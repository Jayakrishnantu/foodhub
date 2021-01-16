package com.foodhub.entity;

import com.foodhub.repository.RestaurantRepository;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Menu Item Entity Object
 */
@Entity
@Table(name="items")
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="item_name")
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="item_price")
    private BigDecimal price;

    @Column(name="prep_time")
    private Integer prepTime;

    @ManyToOne(cascade = {CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public MenuItem() {
    }

    public MenuItem(String name, String description,
                    BigDecimal price, Integer prepTime, Restaurant restaurant) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.prepTime = prepTime;
        this.restaurant = restaurant;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
