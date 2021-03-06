package com.foodhub.payload;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * Payload Carrying Order Create Request
 */
public class OrderCreateRequest {

    @NotNull(message = "Customer Id cannot be empty.")
    private long customerId;

    @NotEmpty(message = "Price cannot be empty.")
    @Size(max= 255, message = "Address cannot exceed 255 characters.")
    private String address;

    @Size(min=1, message = "At least one Order Item must be present.")
    private List<OrderCreateItem> items;

    @NotNull(message = "Distance cannot be empty.")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal distance;

    private String instruction;

    @NotNull
    private Long restaurantId;

    public OrderCreateRequest(int customerId, String address,
                              List<OrderCreateItem> items, Long restaurantIdm, String instruction) {
        this.customerId = customerId;
        this.address = address;
        this.items = items;
        this.restaurantId = restaurantId;
        this.instruction = instruction;
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

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }
}
