package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    @Query("select o from OrderItem o where o.order.orderId = ?1")
    List<OrderItem> findByOrder_OrderId(Long orderId);
}
