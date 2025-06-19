package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpinHistoryRepository extends JpaRepository<SpinHistory, Integer> {
    List<SpinHistory> findByUser(User user);
    
    List<SpinHistory> findByUserAndRedeemed(User user, Boolean redeemed);
}
