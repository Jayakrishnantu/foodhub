package com.foodhub.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Payload Carrying Item Request
 */
public class MenuItemRequest {

    @NotBlank(message="Item name cannot be blank")
    @Size(min=5, max=50, message = "Item name must be of 5-20 characters.")
    private String name;

    @Size(max=255, message = "Description cannot exceed 255 characters.")
    private String description;

    @NotNull(message = "Price cannot be empty.")
    private BigDecimal price;

    @NotNull(message = "Prep Time cannot be empty.")
    private Integer prepTime;

    @NotNull
    private Long restaurantId;

    public MenuItemRequest() {
    }

    public MenuItemRequest(String name, String description, BigDecimal price, Integer prepTime, Long restaurantId) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.prepTime = prepTime;
        this.restaurantId = restaurantId;
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

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
