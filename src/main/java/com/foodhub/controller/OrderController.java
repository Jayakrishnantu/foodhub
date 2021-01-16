package com.foodhub.controller;

import ch.qos.logback.core.CoreConstants;
import com.foodhub.entity.Order;
import com.foodhub.exception.OrderCreateException;
import com.foodhub.exception.OrderNotFoundException;
import com.foodhub.payload.DeliveryOrderResponse;
import com.foodhub.payload.OrderCancelRequest;
import com.foodhub.payload.OrderCancelResponse;
import com.foodhub.payload.OrderCreateRequest;
import com.foodhub.payload.OrderCreateResponse;
import com.foodhub.payload.OrderResponse;
import com.foodhub.payload.OrderStatusUpdateRequest;
import com.foodhub.payload.OrderStatusUpdateResponse;
import com.foodhub.security.JWTokenGenerator;
import com.foodhub.services.OrderService;
import com.foodhub.util.HubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private Logger logger = LoggerFactory.getLogger(OrderController.class.getName());

    @Autowired
    private OrderService service;

    @Autowired
    private JWTokenGenerator tokenGenerator;

    @Autowired
    private HubUtil hubUtil;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request,
                                                           @RequestHeader("Authorization") String jwToken){

        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        request.setCustomerId(userId);

        Order order = service.createOrder(request);
        if(null != order && 0 != order.getOrderId()) {
            logger.info("Order Created.");
            //TODO: Move to Util
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

    @PostMapping("/createfor")
    @PreAuthorize("hasRole('SHOP')")
    public ResponseEntity<OrderCreateResponse> createOrderOnBehalf(@RequestBody OrderCreateRequest request,
                                                                   @RequestHeader("Authorization") String jwToken){

        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        Order order = service.createOrderOnBehalf(request, userId);

        if(null != order && 0 != order.getOrderId()) {
            logger.info("Order Created.");
            // TODO: Move to Util
            logger.info(order.getRestaurant().toString());
            OrderCreateResponse response = new OrderCreateResponse(order.getOrderId(),
                    order.getPrepTime(),
                    order.getDeliveryTime(),
                    "Order Confirmed.",
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

    @PutMapping("/cancel")
    @PreAuthorize("hasAnyRole('SHOP','CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> createOrder(@RequestBody OrderCancelRequest request,
                                         @RequestHeader("Authorization") String jwToken){

        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        request.setCustomerId(userId);

        boolean result = service.cancelOrder(request);

        OrderCancelResponse response = new OrderCancelResponse();
        response.setOrderId(request.getOrderId());

        if(result) {
            logger.info("Order Cancelled.");
            response.setMessage("Order Cancelled. "+
                    "Please Contact us at (888) FOO-DHUB, if you require further assistance.");
        }else{
            logger.error("Order Creation failed.");
            response.setMessage("Order can not be Cancelled. " +
                    "Please Contact us at (888) FOO-DHUB, if you require further assistance.");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/status")
    @PreAuthorize("hasRole('SHOP')")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderStatusUpdateRequest request){

        OrderStatusUpdateResponse response = new OrderStatusUpdateResponse();

        if(service.statusUpdateOrder(request)){
            logger.info("Order status updated.");
            response.setMessage("Order Status Updated.");
        }else{
            logger.info("Order status NOT updated.");
            response.setMessage("Order Status NOT Updated.");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SHOP')")
    public List<OrderResponse> findOrders(@RequestHeader("Authorization") String jwToken){
        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        return hubUtil.createOrderResponses(service.fetchOrders(userId));
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('SHOP')")
    public List<OrderResponse> findActiveOrders(@RequestHeader("Authorization") String jwToken){
        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        return hubUtil.createOrderResponses(service.fetchActiveOrders(userId));
    }

    @GetMapping("/find/{orderId}")
    @PreAuthorize("hasAnyRole('SHOP','CUSTOMER', 'ADMIN')")
    public OrderResponse getOrderDetails(@RequestHeader("Authorization") String jwToken,
                                         @Valid @PathVariable(value = "orderId") Long orderId){
        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        return hubUtil.createOrderResponse(service.fetchOrderById(userId, orderId));
    }

    @GetMapping("pickup")
    @PreAuthorize("hasAnyRole('SHOP','ADMIN', 'DRIVER')")
    public List<DeliveryOrderResponse> getReadyForPickUpOrders(){
        return hubUtil.createDeliveryResponses(service.fetchReadyForPickUpOrders());
    }

    @PutMapping("/pickup/{orderId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<?> notifyOrderPickUp(@Valid @PathVariable(value = "orderId") Long orderId){
        service.notifyOrderPickUp(orderId);
        return new ResponseEntity<>("Order Pickup Notified.", HttpStatus.OK);
    }

    @PutMapping("/delivery/{orderId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<?> notifyOrderDelivery(@Valid @PathVariable(value = "orderId") Long orderId){
        service.notifyOrderDelivery(orderId);
        return new ResponseEntity<>("Order Delivery Notified.", HttpStatus.OK);
    }
}
