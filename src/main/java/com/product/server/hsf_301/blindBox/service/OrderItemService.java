package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> getAllOrderItemsByOrderId(long orderId);
}
