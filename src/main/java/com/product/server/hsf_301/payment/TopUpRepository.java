package com.product.server.hsf_301.payment;

import com.product.server.hsf_301.payment.model.TopUpHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopUpRepository  extends JpaRepository<TopUpHistory,Integer> {
    List<TopUpHistory> findAllByUser_UserId(Integer userUserId);
}
