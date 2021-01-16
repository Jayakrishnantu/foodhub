package com.foodhub.exception;

/**
 * Exception thrown during Order Status Notify.
 */
public class OrderStatusNotifyException extends RuntimeException{

    public OrderStatusNotifyException(String message) {
        super(message);
    }

    public OrderStatusNotifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderStatusNotifyException(Throwable cause) {
        super(cause);
    }
}
