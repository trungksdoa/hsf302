package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.repository.OrderRepository;
import com.product.server.hsf_301.blindBox.service.OrderService;
import com.product.server.hsf_301.user.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Override
    public List<Order> getOrdersByUser(AppUser user) {
        return orderRepository.findByUser(user);
    }
    
    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public Order saveOrder(UserPrizeItem spinHistory) {
        Order order = new Order();
        List<OrderItem>  orderItems= new ArrayList<>();
        orderItems.add(new OrderItem(order,spinHistory.getSpin().getPrizeItemId(),spinHistory.getSpin().getPrice()));
        order.setOrderItems(orderItems);

        order.setUser(spinHistory.getUser());
        return orderRepository.save(order);
    }

    @Override
    public Order saveOrder(List<UserPrizeItem> spinHistories) {
        Order order = new Order();


        // Sử dụng stream để tạo OrderItems từ SpinHistory list
        List<OrderItem> orderItems = spinHistories.stream()
                .map(spinHistory -> new OrderItem(
                        order,
                        spinHistory.getSpin().getPrizeItemId(),
                        spinHistory.getSpin().getPrice()
                ))
                .collect(Collectors.toList());
        order.setUser(spinHistories.get(0).getUser());
        order.setOrderItems(orderItems);
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }
    
    @Override
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }
}
