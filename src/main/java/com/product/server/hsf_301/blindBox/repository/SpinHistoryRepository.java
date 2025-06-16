package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.blindBox.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpinHistoryRepository extends JpaRepository<SpinHistory, Integer> {
    Page<SpinHistory> findByUser(User user, Pageable pageable);
    
    List<SpinHistory> findByUserAndRedeemed(User user, Boolean redeemed);
}
