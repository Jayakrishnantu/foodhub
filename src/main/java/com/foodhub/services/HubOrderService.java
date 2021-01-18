package com.foodhub.services;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Order;
import com.foodhub.entity.OrderItem;
import com.foodhub.entity.OrderStatus;
import com.foodhub.entity.Restaurant;
import com.foodhub.entity.User;
import com.foodhub.exception.NotAuthorizedException;
import com.foodhub.exception.OrderCreateException;
import com.foodhub.exception.OrderNotFoundException;
import com.foodhub.exception.OrderStatusNotifyException;
import com.foodhub.payload.OrderCancelRequest;
import com.foodhub.payload.OrderCreateItem;
import com.foodhub.payload.OrderCreateRequest;
import com.foodhub.payload.OrderStatusUpdateRequest;
import com.foodhub.repository.MenuItemRepository;
import com.foodhub.repository.OrderRepository;
import com.foodhub.repository.RestaurantRepository;
import com.foodhub.repository.UserRepository;
import com.foodhub.util.HubSecurityUtil;
import com.foodhub.util.HubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for Order Service
 */
@Service
public class HubOrderService implements OrderService{

    private Logger logger = LoggerFactory.getLogger(HubOrderService.class.getName());

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    HubUserDetailsService userDetailsService;

    @Autowired
    HubSecurityUtil securityUtil;

    @Autowired
    HubUtil hubUtil;

    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest request) {

        User user = userRepository
                .findById(request.getCustomerId())
                .orElseThrow( () -> new OrderCreateException(
                        hubUtil.readMessage("hub.invalid.customer.info"))
        );
        Restaurant restaurant = restaurantRepository
                .findById(request.getRestaurantId())
                .orElseThrow(()-> new OrderCreateException(
                        hubUtil.readMessage("hub.invalid.restaurant.info"))
        );
        if(null != user.getRestaurantId()
                && ! user.getRestaurantId().equals(restaurant.getRestaurantId())){
            logger.error("Order Create Failed: User is not a customer/shop owner " +
                    "OR shop owner placing order order for a different shop. ");
            throw new NotAuthorizedException(hubUtil.readMessage("hub.order.create.not.auth.restaurant"));
        }
        return saveOrder(request, user, restaurant);
    }

    @Override
    @Transactional
    public Order createOrderOnBehalf(OrderCreateRequest request, Long userId) {

        User user = userRepository
                .findById(request.getCustomerId())
                .orElseThrow(()->new OrderCreateException(
                        hubUtil.readMessage("hub.invalid.customer.info"))
                );
        Restaurant restaurant = restaurantRepository
                .findById(request.getRestaurantId())
                .orElseThrow(()->new OrderCreateException(
                        hubUtil.readMessage("hub.invalid.restaurant.info"))
        );
        User shopUser = userRepository.findById(userId).orElse(null);
        if(null == shopUser
                || ! shopUser.getRestaurantId().equals(restaurant.getRestaurantId())){
            logger.error("Invalid Shop Owner OR Owner is trying to place order on a different shop.");
            throw new NotAuthorizedException(hubUtil.readMessage("hub.order.create.not.auth.restaurant"));
        }
        return saveOrder(request, user, restaurant);
    }

    private Order saveOrder(OrderCreateRequest request, User user, Restaurant restaurant){
        Order order = new Order();

        order.setAddress(request.getAddress());
        order.setStatus(OrderStatus.STATUS_RECEIVED);
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();
        int orderPrepTime = 0;
        BigDecimal orderSubTotal = new BigDecimal(0.0);

        for(OrderCreateItem item: request.getItems()){
            MenuItem menuItem = menuItemRepository.findById(item.getItem()).orElse(null);
            if(null == menuItem
                    || ! menuItem.getRestaurant().getRestaurantId().equals(request.getRestaurantId())){
                logger.error("Item in the request does not belong to the restaurant. Bad request.");
                throw new OrderCreateException(hubUtil.readMessage("hub.order.create.invalid.item.info"));
            }
            if(1 != menuItem.getStatus()){
                logger.error("Item in the request jo longer available on the menu. Bad Request");
                throw new OrderCreateException(hubUtil.readMessage("hub.order.create.item.not.available")+ menuItem.getId());
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQty(item.getQty());
            orderItem.setItem(menuItem);
            orderItem.setInstruction(item.getInstruction());

            int prepTime = hubUtil.findPrepTime(item.getQty(), menuItem.getPrepTime());
            orderPrepTime += prepTime;
            orderItem.setPrepTime(prepTime);
            orderSubTotal = orderSubTotal.add(hubUtil.findItemTotal(item.getQty(), menuItem.getPrice()));
            orderItems.add(orderItem);
        }
        BigDecimal deliveryCharge = request.getDistance().multiply(BigDecimal.ONE);
        BigDecimal orderTax = orderSubTotal.multiply(BigDecimal.valueOf(0.05));
        BigDecimal orderTotal = orderSubTotal.add(orderTax).add(deliveryCharge);
        Integer deliveryTime = request.getDistance()
                .multiply(BigDecimal.valueOf(60)).divide(BigDecimal.valueOf(40)).intValue();
        order.setPrepTime(orderPrepTime);
        order.setDeliveryTime(deliveryTime);
        order.setItemTotal(orderSubTotal);
        order.setDeliveryCharge(deliveryCharge);
        order.setTax(orderTax);
        order.setTotal(orderTotal);
        order.setRestaurant(restaurant);
        order.setOrderItemList(orderItems);
        order.setInstruction(request.getInstruction());
        orderRepository.save(order);
        logger.debug("Order Created without any issues.");
        return order;
    }

    @Override
    @Transactional
    public boolean cancelOrder(OrderCancelRequest request) {

        Order order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(()-> new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.not.found") + request.getOrderId())
        );
        User customer = userRepository
                .findById(request.getCustomerId())
                .orElseThrow(()->new NotAuthorizedException(
                        hubUtil.readMessage("hub.order.update.not.auth")+ request.getOrderId())
        );

        // Check the authorization for cancellation
        // Reject order cancel request if:
        //  1. requested by a different user
        //  2. Not by the Restaurant owner
        //  3. Not by a FoodHub Admin.
        long orderOwnerId = order.getUser().getId();
        if(orderOwnerId != request.getCustomerId()
                && ! (order.getRestaurant().getRestaurantId().equals(customer.getRestaurantId()))
                && ! securityUtil.checkUserIsAdmin(userDetailsService, request.getCustomerId())){
            logger.error("Not Authorized to order: Either requested by a different user, not by shop owner or not by Admin.");
            throw new NotAuthorizedException(hubUtil.readMessage("hub.order.update.not.auth")+ request.getOrderId());
        }
        // Check whether restaurant has already started working on the order
        if(order.getStatus().getValue() != 1){
            logger.debug("Order is already in kitchen or advanced stage; cannot be cancelled.");
            return false;
        }
        // Cancel Order
        order.setStatus(OrderStatus.STATUS_CANCELLED);
        orderRepository.save(order);
        logger.debug("Order Cancelled");

        return true;
    }

    @Override
    @Transactional
    public boolean statusUpdateOrder(OrderStatusUpdateRequest request) {
        Order order = orderRepository
                .findById(request.getOrderId())
                .orElseThrow(()-> new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.not.found")+ request.getOrderId())
        );
        if(request.getOrderStatus().getValue() < order.getStatus().getValue())  {
            logger.debug("Order Status cannot be backtracked.");
            return false;
        }
        order.setStatus(request.getOrderStatus());
        orderRepository.save(order);
        logger.debug("Order Status Updated to "+ request.getOrderStatus());

        return true;
    }

    @Override
    @Transactional
    public List<Order> fetchOrders(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(null == user || null == user.getRestaurantId()){
            logger.error("Invalid User or Restaurant Information.");
            throw new NotAuthorizedException(hubUtil.readMessage("hub.order.get.not.auth"));
        }

        Restaurant restaurant = restaurantRepository
                .findById(user.getRestaurantId())
                .orElseThrow(()->new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.get.invalid.restaurant"))
        );
        return orderRepository.findByRestaurant(restaurant);
    }

    @Override
    @Transactional
    public List<Order> fetchActiveOrders(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(null == user || null == user.getRestaurantId()){
            logger.error("Invalid User or Restaurant Information.");
            throw new NotAuthorizedException(hubUtil.readMessage("hub.order.get.not.auth"));
        }
        Restaurant restaurant = restaurantRepository
                .findById(user.getRestaurantId())
                .orElseThrow(()-> new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.get.invalid.restaurant"))
        );
        return orderRepository.findActiveOrdersByRestaurant(restaurant);
    }

    @Override
    @Transactional
    public Order fetchOrderById(Long userId, Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(()->new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.not.found")+ orderId)
        );
        User user = userRepository
                .findById(userId)
                .orElseThrow(()-> new NotAuthorizedException(
                        hubUtil.readMessage("hub.order.get.not.auth"))
        );
        // Allow the Order access, if:
        // 1. User owns the order
        // 2. Fetched by Restaurant
        // 3. Fetched by FoodHub Admin
        if(order.getUser().getId().equals(userId)
            || (null !=order.getRestaurant().getRestaurantId()
                    && order.getRestaurant().getRestaurantId().equals(user.getRestaurantId()))
            || securityUtil.checkUserIsAdmin(userDetailsService, userId)){
            logger.debug("Order "+ orderId + " is sent back. ");
            return order;
        }
        logger.debug("Order is not sent back. Either user not owns order and not " +
                "fetched by restaurant and not fetched by admin. # "+ orderId);
        throw new NotAuthorizedException(hubUtil.readMessage("hub.order.get.not.auth"));
    }

    @Override
    @Transactional
    public List<Order> fetchReadyForPickUpOrders() {
        logger.debug("Fetching Ready for PickUp orders.");
        return orderRepository.findByStatusEqualsOrderByRestaurant(OrderStatus.STATUS_READY);
    }

    @Override
    @Transactional
    public void notifyOrderPickUp(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.not.found")+ orderId)
        );
        if(3 != order.getStatus().getValue()){
            logger.error("Order is not in READY status for pickup.");
            throw new OrderStatusNotifyException(hubUtil.readMessage("hub.order.not.ready.pickup"));
        }
        order.setStatus(OrderStatus.STATUS_PICKED_UP);
        orderRepository.save(order);

        logger.debug("Order Status changed to "+ OrderStatus.STATUS_PICKED_UP.getDescription());
    }

    @Override
    @Transactional
    public void notifyOrderDelivery(Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.not.found")+ orderId)
        );
        if(4 != order.getStatus().getValue()){
            logger.debug("Order is not picked up yet. "+ orderId);
            throw new OrderStatusNotifyException(hubUtil.readMessage("hub.order.not.picked.up"));
        }
        order.setStatus(OrderStatus.STATUS_DELIVERED);
        orderRepository.save(order);

        logger.debug("Order Status changed to "+ OrderStatus.STATUS_DELIVERED.getDescription());
    }

    @Override
    @Transactional
    public Order fetchInvoiceOrder(Long userId, Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.not.found")+ orderId)
        );
        User user = userRepository
                .findById(userId)
                .orElseThrow(()-> new NotAuthorizedException(
                        hubUtil.readMessage("hub.order.get.not.auth"))
        );
        if(order.getRestaurant().getRestaurantId().equals(user.getRestaurantId())){
            logger.debug("Order details sent back.");
            return order;
        }
        logger.error("Invoice requested for wrong restaurant : "+ order.getRestaurant().getRestaurantId());
        throw new NotAuthorizedException(hubUtil.readMessage("hub.order.get.not.auth"));
    }

    @Override
    @Transactional
    public OrderStatus fetchOrderStatus(Long userId, Long orderId) {
        Order order = orderRepository
                .findById(orderId)
                .orElseThrow(()->new OrderNotFoundException(
                        hubUtil.readMessage("hub.order.not.found")+ orderId)
                );
        User user = userRepository
                .findById(userId)
                .orElseThrow(()-> new NotAuthorizedException(
                        hubUtil.readMessage("hub.order.get.not.auth"))
                );
        // Allow the Order access, if:
        // 1. User owns the order
        // 2. Fetched by Restaurant
        // 3. Fetched by FoodHub Admin
        if(order.getUser().getId().equals(userId)
                || (null !=order.getRestaurant().getRestaurantId()
                && order.getRestaurant().getRestaurantId().equals(user.getRestaurantId()))
                || securityUtil.checkUserIsAdmin(userDetailsService, userId)){
            logger.debug("Order status for "+ orderId + " is sent back. ");
            return order.getStatus();
        }
        logger.debug("Order Status is not sent back. Either user not owns order and not " +
                "fetched by restaurant and not fetched by admin. # "+ orderId);
        throw new NotAuthorizedException(hubUtil.readMessage("hub.order.get.not.auth"));
    }
}
