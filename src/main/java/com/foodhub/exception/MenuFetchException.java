package com.foodhub.exception;

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
