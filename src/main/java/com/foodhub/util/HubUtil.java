package com.foodhub.util;

import com.foodhub.constants.HubConstants;
import com.foodhub.entity.MenuItem;
import com.foodhub.entity.Order;
import com.foodhub.entity.OrderItem;
import com.foodhub.entity.OrderStatus;
import com.foodhub.entity.Restaurant;
import com.foodhub.payload.DeliveryOrderResponse;
import com.foodhub.payload.MenuItemRequest;
import com.foodhub.payload.MenuItemResponse;
import com.foodhub.payload.OrderCreateResponse;
import com.foodhub.payload.OrderResponse;
import com.foodhub.payload.OrderStatusResponse;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class HubUtil implements HubConstants {

    @Autowired
    ReloadableResourceBundleMessageSource hubMessages;

    public MenuItem createMenuItem(MenuItemRequest menuItem, Restaurant restaurant){
        return new MenuItem(menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getPrepTime(),
                restaurant,
                1);
    }

    public List<MenuItemResponse> createMenuItemResponse(List<MenuItem> menuItems){
        List<MenuItemResponse> responses = new ArrayList<>();
        for(MenuItem item: menuItems){
            MenuItemResponse response = new MenuItemResponse(item.getId(), item.getName(),
                    item.getDescription(), item.getPrice(), item.getPrepTime(),
                    item.getRestaurant().getRestaurantId(), item.getRestaurant().getRestaurantName(),
                    item.getStatus());
            responses.add(response);
        }
        return responses;
    }

    public List<OrderResponse> createOrderResponses(List<Order> orders){
        List<OrderResponse> responses = new ArrayList<>();
        for(Order order: orders){
            responses.add(createOrderResponse(order));
        }
        return responses;
    }

    public OrderResponse createOrderResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserName(order.getUser().getUserName());
        response.setDeliverAddress(order.getAddress());
        response.setCustomerContact(order.getUser().getContact());
        response.setSubTotal(order.getItemTotal());
        response.setTax(order.getTax());
        response.setDeliveryCharge(order.getDeliveryCharge());
        response.setTotal(order.getTotal());
        response.setFoodPrepTimeInMinutes(order.getPrepTime());
        response.setDeliveryTimeInMinutes(order.getDeliveryTime());
        response.setOrderStatus(order.getStatus());
        if(null != order.getDeliveryBy()){
            response.setDeliveryPerson(order.getDeliveryBy().getName());
            response.setDeliveryPersonContact(order.getDeliveryBy().getContact());
        }else{
            response.setDeliveryPerson("Not Assigned");
            response.setDeliveryPersonContact("Not Available");
        }
        response.setInstruction(order.getInstruction());
        response.setRestaurantName(order.getRestaurant().getRestaurantName());
        response.setRestaurantAddress(order.getRestaurant().getRestaurantAddress());
        response.setRestaurantContactInfo(order.getRestaurant().getRestaurantContactInfo());
        List<String> items = new ArrayList<>();
        for(OrderItem item: order.getOrderItemList()){
            items.add("Item: "+ item.getItem().getName()
                    +", Qty: "+ item.getQty()
                    +", Instruction: "+ item.getInstruction());
        }
        response.setOrderItems(items);

        return response;
    }

    public List<DeliveryOrderResponse> createDeliveryResponses(List<Order> orders){
        List<DeliveryOrderResponse> responses = new ArrayList<>();
        for(Order order: orders){
            responses.add(createDeliveryOrderResponse(order));
        }
        return responses;
    }


    private DeliveryOrderResponse createDeliveryOrderResponse(Order order){
        DeliveryOrderResponse response = new DeliveryOrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserName(order.getUser().getUserName());
        response.setDeliverAddress(order.getAddress());
        response.setCustomerContact(order.getUser().getContact());

        response.setTotal(order.getTotal());

        response.setRestaurantName(order.getRestaurant().getRestaurantName());
        response.setRestaurantAddress(order.getRestaurant().getRestaurantAddress());
        response.setRestaurantContactInfo(order.getRestaurant().getRestaurantContactInfo());

        return response;
    }

    public String getToken(String token){
        return token.substring(7);
    }

    public Integer findPrepTime(Integer qty, Integer prepTime){
        return Math.multiplyExact(qty, prepTime);
    }

    public BigDecimal findItemTotal(Integer qty, BigDecimal unitPrice){
        return unitPrice.multiply(BigDecimal.valueOf(qty.longValue()));
    }

    public OrderCreateResponse createOrderCreateResponse(Order order, String message){
        OrderCreateResponse response = new OrderCreateResponse(order.getOrderId(),
                order.getPrepTime(),
                order.getDeliveryTime(),
                message,
                order.getItemTotal().setScale(2, RoundingMode.HALF_UP),
                order.getTax().setScale(2, RoundingMode.HALF_UP),
                order.getDeliveryCharge().setScale(2, RoundingMode.HALF_UP),
                order.getTotal().setScale(2, RoundingMode.HALF_UP),
                order.getRestaurant().getRestaurantName(),
                order.getUser().getUserName(),
                order.getInstruction());

        return response;
    }

    public String readMessage(String code){
        return hubMessages.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getItemPrice(OrderItem item){
        return findItemTotal(item.getQty(), item.getItem().getPrice())
                .setScale(2, RoundingMode.HALF_UP).toString();
    }

    public OrderStatusResponse createOrderStatusResponse(Long orderId, OrderStatus status){
        return  new OrderStatusResponse(orderId, status.getDescription());
    }
}
