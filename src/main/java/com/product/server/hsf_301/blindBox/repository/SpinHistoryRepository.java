package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.AppUser;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpinHistoryRepository extends JpaRepository<SpinHistory, Integer> {
    List<SpinHistory> findByUser(AppUser user);
    Page<SpinHistory> findByUser(AppUser user, Pageable pageable);

    List<SpinHistory> findByUserAndRedeemed(AppUser user, Boolean redeemed);
}
