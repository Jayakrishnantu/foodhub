package com.foodhub.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class MenuItemRequest {

    @NotNull
    @Size(max=50)
    private String name;

    @Size(max=255)
    private String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer prepTime;

    public MenuItemRequest() {
    }

    public MenuItemRequest(String name, String description, BigDecimal price, Integer prepTime) {
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
