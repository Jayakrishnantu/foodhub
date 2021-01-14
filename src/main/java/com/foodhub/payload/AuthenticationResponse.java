package com.foodhub.payload;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private String jwToken;

    public AuthenticationResponse(String jwToken) {
        this.jwToken = jwToken;
    }

    public String getJwToken() {
        return jwToken;
    }
}
