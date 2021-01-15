package com.foodhub.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long restaurantId;

    @Column(name="name")
    private String restaurantName;

    @Column(name="address")
    private String restaurantAddress;

    @Column(name="contact")
    private String restaurantContactInfo;

    public Restaurant() {
    }

    public Restaurant(String restaurantName, String restaurantAddress, String restaurantContactInfo) {
        this.restaurantName = restaurantName;
        this.restaurantAddress = restaurantAddress;
        this.restaurantContactInfo = restaurantContactInfo;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
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

    @Override
    public String toString() {
        return "Restaurant{" +
                "restaurantId=" + restaurantId +
                ", restaurantName='" + restaurantName + '\'' +
                ", restaurantAddress='" + restaurantAddress + '\'' +
                ", restaurantContactInfo='" + restaurantContactInfo + '\'' +
                '}';
    }
}
