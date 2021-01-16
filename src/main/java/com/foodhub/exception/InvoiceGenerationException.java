package com.foodhub.exception;

/**
 * Exception thrown during Invoice Generation
 */
public class InvoiceGenerationException extends RuntimeException{
    public InvoiceGenerationException(String message) {
        super(message);
    }

    public InvoiceGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvoiceGenerationException(Throwable cause) {
        super(cause);
    }
}
