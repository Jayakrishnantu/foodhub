package com.foodhub.services;

import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Order;
import com.foodhub.entity.OrderItem;
import com.foodhub.entity.OrderStatus;
import com.foodhub.entity.Restaurant;
import com.foodhub.entity.User;
import com.foodhub.exception.OrderCreateException;
import com.foodhub.exception.OrderNotFoundException;
import com.foodhub.payload.OrderCancelRequest;
import com.foodhub.payload.OrderCreateItem;
import com.foodhub.payload.OrderCreateRequest;
import com.foodhub.repository.MenuItemRepository;
import com.foodhub.repository.OrderRepository;
import com.foodhub.repository.RestaurantRepository;
import com.foodhub.repository.UserRepository;
import com.foodhub.security.HubUserDetailsService;
import com.foodhub.util.HubSecurityUtil;
import com.foodhub.util.HubUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAuthorizedException;
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

    @Override
    public Order createOrder(OrderCreateRequest request) {

        User user = userRepository.findById(request.getCustomerId()).orElse(null);
        if(null == user){
            throw new OrderCreateException("Invalid customer information.");
        }

        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId()).orElse(null);
        if(null == restaurant){
            throw new OrderCreateException("Invalid Restaurant information.");
        }

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

            int prepTime = HubUtil.findPrepTime(item.getQty(), menuItem.getPrepTime());
            orderPrepTime += prepTime;
            orderItem.setPrepTime(prepTime);

            orderSubTotal = orderSubTotal.add(HubUtil.findItemTotal(item.getQty(), menuItem.getPrice()));

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
    public boolean cancelOrder(OrderCancelRequest request) {

        Order order = orderRepository.findById(request.getOrderId()).orElse(null);

        // Order not found in database
        if(null == order){
           throw new OrderNotFoundException("Order Not Found : Order # "+ request.getOrderId());
        }

        // Check the ownership of the order, allow to proceed only
        // if it's cancelled by real customer or restaurant
        long orderOwnerId = order.getUser().getId();
        if(orderOwnerId != request.getCustomerId()
                && ! HubSecurityUtil.checkUserIsAdmin(userDetailsService, request.getCustomerId())){
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
}
