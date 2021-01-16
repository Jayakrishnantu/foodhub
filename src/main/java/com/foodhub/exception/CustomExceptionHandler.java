package com.foodhub.exception;

import com.foodhub.util.HubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Controller Advice for handling various exceptions thrown from Food Hub Service
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class.getName());

    @Autowired
    HubUtil hubUtil;

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErrorMessage> handleAccessDeniedException(AccessDeniedException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setMessage(hubUtil.readMessage("hub.access.denied"));
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

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

    @ExceptionHandler(NotAuthorizedException.class)
    public final ResponseEntity<ErrorMessage> handleOrderCancelException(NotAuthorizedException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(hubUtil.readMessage("hub.invalid.input.parameters"));
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderStatusNotifyException.class)
    public final ResponseEntity<ErrorMessage> handleOrderStatusNotifyException(
            OrderStatusNotifyException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvoiceGenerationException.class)
    public final ResponseEntity<ErrorMessage> handleInvoiceGenerationException(
            InvoiceGenerationException exception) {

        logger.error(exception.getMessage());

        ErrorMessage response = new ErrorMessage();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(exception.getMessage());
        response.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
