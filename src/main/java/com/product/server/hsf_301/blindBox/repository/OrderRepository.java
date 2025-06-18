package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.Order;
import com.product.server.hsf_301.blindBox.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(AppUser user);
    
    List<Order> findByStatus(String status);
    
    List<Order> findByUserAndStatus(AppUser user, String status);
}
