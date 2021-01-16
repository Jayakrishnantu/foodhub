package com.foodhub.payload;

import java.math.BigDecimal;

/**
 * Payload Carrying Menu Item Response
 */
public class MenuItemResponse {

    private Long itemId;
    private String itemName;
    private String description;
    private BigDecimal price;
    private Integer prepTime;
    private Long restaurantId;
    private String restaurantName;

    public MenuItemResponse() {
    }

    public MenuItemResponse(Long itemId, String itemName,
                            String description, BigDecimal price,
                            Integer prepTime, Long restaurantId,
                            String restaurantName) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.description = description;
        this.price = price;
        this.prepTime = prepTime;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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
}
