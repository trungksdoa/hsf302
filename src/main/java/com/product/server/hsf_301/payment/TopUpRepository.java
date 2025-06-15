package com.product.server.hsf_301.payment;

import com.product.server.hsf_301.payment.model.TopUpHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopUpRepository  extends JpaRepository<TopUpHistory,Integer> {
}
