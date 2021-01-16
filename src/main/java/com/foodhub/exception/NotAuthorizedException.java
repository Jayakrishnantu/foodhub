package com.foodhub.exception;

/**
 * Exception thrown if the action is not authorized.
 */
public class NotAuthorizedException extends RuntimeException{
    public NotAuthorizedException(String message) {
        super(message);
    }

    public NotAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthorizedException(Throwable cause) {
        super(cause);
    }
}
