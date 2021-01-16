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
import com.foodhub.security.HubUserDetailsService;
import com.foodhub.util.HubSecurityUtil;
import com.foodhub.util.HubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class HubOrderService implements OrderService{

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
            if(null == menuItem ||
                    ! menuItem.getRestaurant().getRestaurantId().equals(request.getRestaurantId())){
                throw new OrderCreateException(hubUtil.readMessage("hub.invalid.item.info"));
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQty(item.getQty());
            orderItem.setItem(menuItem);
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
        order.setOrderItemsList(orderItems);
        orderRepository.save(order);

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
                && ! customer.getRestaurantId().equals(order.getRestaurant().getRestaurantId())
                && ! securityUtil.checkUserIsAdmin(userDetailsService, request.getCustomerId())){
            throw new NotAuthorizedException(hubUtil.readMessage("hub.order.update.not.auth")+ request.getOrderId());
        }
        // Check whether restaurant has already started working on the order
        if(order.getStatus().getValue() != 1){
            return false;
        }
        // Cancel Order
        order.setStatus(OrderStatus.STATUS_CANCELLED);
        orderRepository.save(order);

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
        if(request.getOrderStatus().getValue() < order.getStatus().getValue())  return false;
        order.setStatus(request.getOrderStatus());
        orderRepository.save(order);

        return true;
    }

    @Override
    @Transactional
    public List<Order> fetchOrders(Long userId) {

        User user = userRepository.findById(userId).orElse(null);
        if(null == user || null == user.getRestaurantId()){
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
            return order;
        }
        throw new NotAuthorizedException(hubUtil.readMessage("hub.order.get.not.auth"));
    }

    @Override
    @Transactional
    public List<Order> fetchReadyForPickUpOrders() {
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
            throw new OrderStatusNotifyException(hubUtil.readMessage("hub.order.not.ready.pickup"));
        }
        order.setStatus(OrderStatus.STATUS_PICKED_UP);
        orderRepository.save(order);
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
            throw new OrderStatusNotifyException(hubUtil.readMessage("hub.order.not.picked.up"));
        }
        order.setStatus(OrderStatus.STATUS_DELIVERED);
        orderRepository.save(order);
    }
}
