package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.Order;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.blindBox.model.UserPrizeItem;
import com.product.server.hsf_301.user.model.AppUser;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    
    Order getOrderById(Integer id);
    
    List<Order> getOrdersByUser(AppUser user);
    
    List<Order> getOrdersByStatus(String status);
    
    Order saveOrder(UserPrizeItem spinHistory);
    Order saveOrder(List<UserPrizeItem> spinHistories);


    Order updateOrderStatus(Integer orderId, String status);
    
    void deleteOrder(Integer orderId);
}
