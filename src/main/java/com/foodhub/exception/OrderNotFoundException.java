package com.foodhub.exception;

/**
 * Exception thrown when the Order Not found in system
 */
public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(String message) {
        super(message);
    }

    public OrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderNotFoundException(Throwable cause) {
        super(cause);
    }
}
