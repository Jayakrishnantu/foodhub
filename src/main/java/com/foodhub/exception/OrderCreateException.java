package com.foodhub.exception;

public class OrderCreateException extends RuntimeException{

    public OrderCreateException(String message) {
        super(message);
    }

    public OrderCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderCreateException(Throwable cause) {
        super(cause);
    }
}
