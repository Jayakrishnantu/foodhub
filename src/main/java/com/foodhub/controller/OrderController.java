package com.foodhub.controller;

import com.foodhub.entity.Order;
import com.foodhub.exception.OrderCreateException;
import com.foodhub.payload.OrderCancelRequest;
import com.foodhub.payload.OrderCancelResponse;
import com.foodhub.payload.OrderCreateRequest;
import com.foodhub.payload.OrderCreateResponse;
import com.foodhub.security.JWTokenGenerator;
import com.foodhub.services.OrderService;
import com.foodhub.util.HubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class.getName());

    @Autowired
    private OrderService service;

    @Autowired
    private JWTokenGenerator tokenGenerator;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('SHOP','CUSTOMER')")
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request,
                                                           @RequestHeader("Authorization") String jwToken){

        long userId = tokenGenerator.getUserIdFromJWT(HubUtil.getToken(jwToken));
        request.setCustomerId(userId);

        Order order = service.createOrder(request);
        if(null != order && 0 != order.getOrderId()) {
            logger.info("Order Created.");

            logger.info(order.getRestaurant().toString());

            OrderCreateResponse response = new OrderCreateResponse(order.getOrderId(),
                        order.getPrepTime(),
                        order.getDeliveryTime(),
                         "Order Confirmed. Please Contact us at (888) FOO-DHUB in case of any questions/concerns.",
                        order.getItemTotal().setScale(2, RoundingMode.HALF_UP),
                        order.getTax().setScale(2, RoundingMode.HALF_UP),
                        order.getDeliveryCharge().setScale(2, RoundingMode.HALF_UP),
                        order.getTotal().setScale(2, RoundingMode.HALF_UP),
                        order.getRestaurant().getRestaurantName(),
                        order.getUser().getUserName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            logger.error("Order Creation failed.");
            throw new OrderCreateException("Issues with Order Creation, Please try again");
        }
    }

    @PostMapping("/cancel")
    @PreAuthorize("hasAnyRole('SHOP','CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> createOrder(@RequestBody OrderCancelRequest request,
                                         @RequestHeader("Authorization") String jwToken){

        long userId = tokenGenerator.getUserIdFromJWT(HubUtil.getToken(jwToken));
        request.setCustomerId(userId);

        boolean result = service.cancelOrder(request);

        OrderCancelResponse response = new OrderCancelResponse();
        response.setOrderId(request.getOrderId());

        if(result) {
            logger.info("Order Cancelled.");
            response.setMessage("Order Cancelled - Order # "+ request.getOrderId());
        }else{
            logger.error("Order Creation failed.");
            response.setMessage("Order can not be Cancelled. " +
                    "Please Contact us at (888) FOO-DHUB, if you require further assistance.");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
