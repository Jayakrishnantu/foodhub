package com.foodhub.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.NotAuthorizedException;
import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class.getName());

    /**
     * Handling the Access Denied Exception
     * @param exception
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setMessage("Access Denied: Insufficient privileges.");
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handling the Order Create Failure
     * @param exception
     * @return
     */
    @ExceptionHandler(OrderCreateException.class)
    public final ResponseEntity<ErrorMessage> handleOrderCreateException(OrderCreateException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public final ResponseEntity<ErrorMessage> handleOrderNotFoundException(OrderNotFoundException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public final ResponseEntity<ErrorMessage> handleNotAuthorizedException(NotAuthorizedException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MenuItemCreateException.class)
    public final ResponseEntity<ErrorMessage> handleMenuItemCreateException(MenuItemCreateException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MenuFetchException.class)
    public final ResponseEntity<ErrorMessage> handleMenuFetchException(MenuFetchException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
