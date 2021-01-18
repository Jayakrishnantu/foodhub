package com.foodhub.payload;

import com.foodhub.entity.UserRole;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank
    @Size(min=5, message ="Username must have at least 3 characters")
    private String name;

    @NotNull
    @Size(min=5, message ="Username must have at least 5 characters")
    private String username;

    @NotNull
    @Size(min=5, message ="Password must have at least 5 characters")
    private String password;

    @NotNull
    @Size(min=10, message ="Contact must have at least 10 characters")
    private String contact;

    /**
     * Possible Values
     *  - customer, admin, shop, driver
     */
    @NotEmpty(message = "User type must be specified.")
    private String userType;

    private Long restaurantId;

    private UserRole role;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String username,
                           String password, String contact,
                           Long restaurantId,
                           String userType) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.contact = contact;
        this.restaurantId = restaurantId;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
