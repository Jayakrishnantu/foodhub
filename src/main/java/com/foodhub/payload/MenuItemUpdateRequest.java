package com.foodhub.payload;

import com.sun.istack.NotNull;

import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Payload Carrying Item Request
 */
public class MenuItemUpdateRequest {

    @NotNull
    private String name;

    @Size(max=255,message = "Description cannot exceed 255 characters.")
    private String description;

    private BigDecimal price;

    private Integer prepTime;

    public MenuItemUpdateRequest() {
    }

    public MenuItemUpdateRequest(String name, String description, BigDecimal price, Integer prepTime) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.prepTime = prepTime;
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
}
