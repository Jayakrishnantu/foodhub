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

        User user = userRepository.findById(request.getCustomerId()).orElse(null);
        if(null == user){
            throw new OrderCreateException("Invalid customer information.");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElse(null);
        if(null == restaurant){
            throw new OrderCreateException("Invalid Restaurant information.");
        }

        if(null != user.getRestaurantId()
                && ! user.getRestaurantId().equals(restaurant.getRestaurantId())){
            throw new NotAuthorizedException("Not Authorized to Create Order for this Restaurant");
        }

        return saveOrder(request, user, restaurant);
    }

    @Override
    @Transactional
    public Order createOrderOnBehalf(OrderCreateRequest request, Long userId) {

        User user = userRepository.findById(request.getCustomerId()).orElse(null);
        if(null == user){
            throw new OrderCreateException("Invalid customer information.");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElse(null);
        if(null == restaurant){
            throw new OrderCreateException("Invalid Restaurant information.");
        }

        User shopUser = userRepository.findById(userId).orElse(null);

        if(! shopUser.getRestaurantId().equals(restaurant.getRestaurantId())){
            throw new NotAuthorizedException("Not Authorized to Create Order for this Restaurant");
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
                throw new OrderCreateException("Invalid Item information.");
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
        Integer deliveryTime = request.getDistance().multiply(BigDecimal.valueOf(60)).divide(BigDecimal.valueOf(40)).intValue();

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

        Order order = orderRepository.findById(request.getOrderId()).orElse(null);

        // Order not found in database
        if(null == order){
           throw new OrderNotFoundException("Order Not Found : Order # "+ request.getOrderId());
        }

        User customer = userRepository.findById(request.getCustomerId()).orElse(null);
        if(null == customer){
            throw new NotAuthorizedException("Not Authorized to update Order # : "+ request.getOrderId());
        }

        // Check the authorization for cancellation
        // Reject order cancel request if:
        //  1. requested by a different user
        //  2. Not by the Restaurant owner
        //  3. Not by a FoodHub Admin.
        long orderOwnerId = order.getUser().getId();
        if(orderOwnerId != request.getCustomerId()
                && ! customer.getRestaurantId().equals(order.getRestaurant().getRestaurantId())
                && ! securityUtil.checkUserIsAdmin(userDetailsService, request.getCustomerId())){
            throw new NotAuthorizedException("Not Authorized to update Order # : "+ request.getOrderId());
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

        Order order = orderRepository.findById(request.getOrderId()).orElse(null);
        if(null == order){
            throw new OrderNotFoundException("Order Not Found : Order # "+ request.getOrderId());
        }

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
            throw new NotAuthorizedException("Not authorized to pull orders.");
        }

        Restaurant restaurant = restaurantRepository.findById(user.getRestaurantId()).orElse(null);
        if(null == restaurant) {
            throw new OrderNotFoundException("Invalid Restaurant - orders not found. ");
        }
        return orderRepository.findByRestaurant(restaurant);
    }

    @Override
    @Transactional
    public List<Order> fetchActiveOrders(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(null == user || null == user.getRestaurantId()){
            throw new NotAuthorizedException("Not authorized to pull orders.");
        }
        Restaurant restaurant = restaurantRepository.findById(user.getRestaurantId()).orElse(null);
        if(null == restaurant) {
            throw new OrderNotFoundException("Invalid Restaurant - orders not found. ");
        }
        return orderRepository.findActiveOrdersByRestaurant(restaurant);
    }

    @Override
    @Transactional
    public Order fetchOrderById(Long userId, Long orderId) {

        User user = userRepository.findById(userId).orElse(null);
        if(null == user){
            throw new NotAuthorizedException("Not authorized to pull order.");
        }
        Order order = orderRepository.findById(orderId).orElse(null);
        if(null == order){
            throw new OrderNotFoundException("Order Not Found : Order # "+ orderId);
        }
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
        throw new NotAuthorizedException("Not authorized to pull order.");
    }

    @Override
    @Transactional
    public List<Order> fetchReadyForPickUpOrders() {
        return orderRepository.findByStatusEqualsOrderByRestaurant(OrderStatus.STATUS_READY);
    }

    @Override
    @Transactional
    public void notifyOrderPickUp(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(null == order){
            throw new OrderNotFoundException("Order Not Found : Order # "+ orderId);
        }
        if(3 != order.getStatus().getValue()){
            throw new OrderStatusNotifyException("Order not Ready for Pick Up");
        }
        order.setStatus(OrderStatus.STATUS_PICKED_UP);
        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void notifyOrderDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if(null == order){
            throw new OrderNotFoundException("Order Not Found : Order # "+ orderId);
        }
        if(4 != order.getStatus().getValue()){
            throw new OrderStatusNotifyException("Order NOT Picked Up");
        }
        order.setStatus(OrderStatus.STATUS_DELIVERED);
        orderRepository.save(order);
    }
}
