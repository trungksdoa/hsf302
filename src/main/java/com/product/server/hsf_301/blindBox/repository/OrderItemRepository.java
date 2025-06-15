package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
   
}
