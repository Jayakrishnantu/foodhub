package com.foodhub.payload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RestaurantRequest {

    @NotEmpty(message = "Restaurant name cannot be empty")
    @Size(min=5, max=100, message="Restaurant name must have 5-100 characters.")
    private String name;

    @NotEmpty(message = "Restaurant address cannot be empty")
    @Size(min=10, max=100, message="Restaurant address must have 10-255 characters.")
    private String address;

    @NotEmpty(message = "Restaurant contact cannot be empty")
    @Size(min=10, max=255, message="Restaurant constant must have 10-255 characters.")
    private String contact;

    public RestaurantRequest() {
    }

    public RestaurantRequest(String name, String address, String contact) {
        this.name = name;
        this.address = address;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
