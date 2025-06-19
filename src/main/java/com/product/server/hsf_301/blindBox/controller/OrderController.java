package com.product.server.hsf_301.blindBox.controller;

import com.product.server.hsf_301.blindBox.model.Order;
import com.product.server.hsf_301.user.model.User;
import com.product.server.hsf_301.blindBox.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public String getAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @GetMapping("/{id}")
    public String getOrderById(@PathVariable Integer id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order/details";
    }

    @GetMapping("/create")
    public String createOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order/create";
    }

    @PostMapping("/create")
    public String createOrder(@ModelAttribute Order order) {
        orderService.saveOrder(order);
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String editOrderForm(@PathVariable Integer id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateOrder(@PathVariable Integer id, @ModelAttribute Order order) {
        order.setOrderId(id);
        orderService.saveOrder(order);
        return "redirect:/orders";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/orders";
    }
    
    @GetMapping("/user/{userId}")
    public String getOrdersByUser(@PathVariable Integer userId, Model model) {
        User user = new User();
        user.setUserId(userId);
        
        List<Order> orders = orderService.getOrdersByUser(user);
        model.addAttribute("orders", orders);
        model.addAttribute("userId", userId);
        return "order/userOrders";
    }
    
    @GetMapping("/status/{status}")
    public String getOrdersByStatus(@PathVariable String status, Model model) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        model.addAttribute("orders", orders);
        model.addAttribute("status", status);
        return "order/statusOrders";
    }
    
    @GetMapping("/updateStatus/{id}")
    public String updateOrderStatusForm(@PathVariable Integer id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order/updateStatus";
    }
    
    @PostMapping("/updateStatus/{id}")
    public String updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/orders";
    }
}
