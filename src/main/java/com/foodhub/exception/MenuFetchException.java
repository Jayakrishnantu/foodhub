package com.foodhub.exception;

/**
 * Exception thrown during Fetching of Restaurant Menu
 */
public class MenuFetchException extends RuntimeException{
    public MenuFetchException(String message) {
        super(message);
    }

    public MenuFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuFetchException(Throwable cause) {
        super(cause);
    }
}
