package com.foodhub.payload;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Payload Carrying Order Create Request
 */
public class OrderCreateRequest {

    @NotNull
    private long customerId;

    @NotNull
    @Size(max= 255)
    private String address;

    @Size(min=1)
    private List<OrderCreateItem> items;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal distance;

    @NotNull
    private Long restaurantId;

    public OrderCreateRequest(int customerId, String address,
                              List<OrderCreateItem> items, Long restaurantId) {
        this.customerId = customerId;
        this.address = address;
        this.items = items;
        this.restaurantId = restaurantId;
    }

    public OrderCreateRequest() {
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<OrderCreateItem> getItems() {
        return items;
    }

    public void setItems(List<OrderCreateItem> items) {
        this.items = items;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
