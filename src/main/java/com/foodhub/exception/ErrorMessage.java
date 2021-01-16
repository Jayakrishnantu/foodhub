package com.foodhub.exception;

/**
 * POJO class for holding Error Send Back to Clients.
 * Carries:
 *  HTTP Status Code
 *  Error Message
 *  Timestamp
 */
public class ErrorMessage {

    private int status;
    private String message;
    private long timestamp;

    public ErrorMessage() {
    }

    public ErrorMessage(int status, String message, long timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
