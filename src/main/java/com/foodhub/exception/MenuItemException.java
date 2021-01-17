package com.foodhub.exception;

/**
 * Exception thrown during Menu Item Create
 */
public class MenuItemException extends RuntimeException{
    public MenuItemException(String message) {
        super(message);
    }

    public MenuItemException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuItemException(Throwable cause) {
        super(cause);
    }
}
