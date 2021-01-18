package com.foodhub.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Payload Carrying Authentication Request
 */
public class AuthenticationRequest implements Serializable {

    @NotBlank(message="Usename cannot be blank")
    @Size(min =5, max=50, message = "Username must be of 5-50 characters.")
    private String userName;

    @NotBlank(message="Password cannot be blank")
    @Size(min=5, max = 20, message = "Password must be of 5-20 characters.")
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
