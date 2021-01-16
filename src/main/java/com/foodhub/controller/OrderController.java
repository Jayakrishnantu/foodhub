package com.foodhub.controller;

import com.foodhub.entity.Order;
import com.foodhub.exception.OrderCreateException;
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
import com.foodhub.util.HubInvoiceUtil;
import com.foodhub.util.HubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.util.List;

/**
 * Controller handling all Order related requests.
 */
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

    @Autowired
    HubInvoiceUtil invoiceUtil;

    @PostMapping("/create")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request,
                                                           @RequestHeader("Authorization") String jwToken){

        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        request.setCustomerId(userId);

        Order order = service.createOrder(request);
        if(null != order && 0 != order.getOrderId()) {
            logger.debug("Order Created."+ order.getRestaurant().toString());
            return new ResponseEntity<>(hubUtil.createOrderCreateResponse(order,
                    hubUtil.readMessage("hub.order.create.success")), HttpStatus.OK);
        }else{
            logger.error("Order Creation failed.");
            throw new OrderCreateException("Issues with Order Creation, Please try again");
        }
    }

    @PostMapping("/createfor")
    @PreAuthorize("hasRole('SHOP')")
    public ResponseEntity<OrderCreateResponse> createOrderOnBehalf(
            @RequestBody OrderCreateRequest request,
            @RequestHeader("Authorization") String jwToken){

        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        Order order = service.createOrderOnBehalf(request, userId);

        if(null != order && 0 != order.getOrderId()) {
            logger.debug("Order Created On behalf. "+ order.getOrderId());
            return new ResponseEntity<>(hubUtil.createOrderCreateResponse(order,
                    hubUtil.readMessage("hub.order.create.on.behalf.success")), HttpStatus.OK);
        }else{
            logger.error("Failed to create order on behalf.");
            throw new OrderCreateException(hubUtil.readMessage("hub.order.create.failure"));
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
            logger.debug("Order Cancelled.");
            response.setMessage(hubUtil.readMessage("hub.order.cancel.success"));
        }else{
            logger.error("Order Creation failed - "+ request.getOrderId());
            response.setMessage(hubUtil.readMessage("hub.order.cancel.failure"));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/status")
    @PreAuthorize("hasRole('SHOP')")
    public ResponseEntity<?> updateOrderStatus(@RequestBody OrderStatusUpdateRequest request){

        OrderStatusUpdateResponse response = new OrderStatusUpdateResponse();

        if(service.statusUpdateOrder(request)){
            logger.debug("Order status updated.");
            response.setMessage(hubUtil.readMessage("hub.order.status.update.success"));
        }else{
            logger.error("Order status NOT updated "+ request.getOrderId());
            response.setMessage(hubUtil.readMessage("hub.order.status.update.failure"));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SHOP')")
    public List<OrderResponse> findOrders(@RequestHeader("Authorization") String jwToken){
        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        logger.debug("Pulling all the orders.");
        return hubUtil.createOrderResponses(service.fetchOrders(userId));
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('SHOP')")
    public List<OrderResponse> findActiveOrders(@RequestHeader("Authorization") String jwToken){
        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        logger.debug("Pulling all the active orders.");
        return hubUtil.createOrderResponses(service.fetchActiveOrders(userId));
    }

    @GetMapping("/find/{orderId}")
    @PreAuthorize("hasAnyRole('SHOP','CUSTOMER', 'ADMIN')")
    public OrderResponse getOrderDetails(@RequestHeader("Authorization") String jwToken,
                                         @Valid @PathVariable(value = "orderId") Long orderId){
        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        logger.debug("Finding the order : "+ orderId);
        return hubUtil.createOrderResponse(service.fetchOrderById(userId, orderId));
    }

    @GetMapping("pickup")
    @PreAuthorize("hasAnyRole('SHOP','ADMIN', 'DRIVER')")
    public List<DeliveryOrderResponse> getReadyForPickUpOrders(){
        logger.debug("Finding Ready for pick up orders.");
        return hubUtil.createDeliveryResponses(service.fetchReadyForPickUpOrders());
    }

    @PutMapping("/pickup/{orderId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<?> notifyOrderPickUp(@Valid @PathVariable(value = "orderId") Long orderId){
        service.notifyOrderPickUp(orderId);
        logger.debug("Order pick up notified. # "+ orderId);
        return new ResponseEntity<>(hubUtil.readMessage("hub.order.pickup.notify"), HttpStatus.OK);
    }

    @PutMapping("/delivery/{orderId}")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<?> notifyOrderDelivery(@Valid @PathVariable(value = "orderId") Long orderId){
        service.notifyOrderDelivery(orderId);
        logger.debug("Order delivery notified. # "+ orderId);
        return new ResponseEntity<>(hubUtil.readMessage("hub.order.delivery.notify"), HttpStatus.OK);
    }

    @GetMapping("/invoice/{orderId}")
    @PreAuthorize("hasRole('SHOP')")
    public ResponseEntity<?> generateInvoice(@Valid @PathVariable(value = "orderId") Long orderId,
                                             @RequestHeader("Authorization") String jwToken){
        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        Order order = service.fetchInvoiceOrder(userId, orderId);
        ByteArrayInputStream pdfStream = invoiceUtil.generatePdfStream(order);
        logger.debug("Invoice generate for order "+ orderId);

        String fileName = "invoice_"+orderId+".pdf";
        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename="+fileName);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfStream));
    }

    @GetMapping("/status/{orderId}")
    @PreAuthorize("hasAnyRole('SHOP','CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> fetchOrderStatus(@Valid @PathVariable(value = "orderId") Long orderId,
                                              @RequestHeader("Authorization") String jwToken){
        long userId = tokenGenerator.getUserIdFromJWT(hubUtil.getToken(jwToken));
        return new ResponseEntity<>(hubUtil
                .createOrderStatusResponse(orderId, service.fetchOrderStatus(userId, orderId)),
                HttpStatus.OK );
    }
}
