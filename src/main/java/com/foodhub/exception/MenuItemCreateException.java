package com.foodhub.exception;

/**
 * Exception thrown during Menu Item Create
 */
public class MenuItemCreateException extends RuntimeException{
    public MenuItemCreateException(String message) {
        super(message);
    }

    public MenuItemCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuItemCreateException(Throwable cause) {
        super(cause);
    }
}
