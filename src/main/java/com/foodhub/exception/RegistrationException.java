package com.foodhub.exception;

/**
 * Exception thrown during Registration of a User
 */
public class RegistrationException extends RuntimeException{
    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationException(Throwable cause) {
        super(cause);
    }
}
