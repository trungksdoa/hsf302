package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.OrderItem;
import com.product.server.hsf_301.blindBox.repository.OrderItemRepository;
import com.product.server.hsf_301.blindBox.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    @Override
    public List<OrderItem> getAllOrderItemsByOrderId(long orderId) {
        return orderItemRepository.findByOrder_OrderId(orderId);
    }
}
