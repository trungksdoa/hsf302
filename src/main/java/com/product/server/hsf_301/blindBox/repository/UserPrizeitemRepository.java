package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.UserPrizeItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPrizeitemRepository extends JpaRepository<UserPrizeItem,Long> {
    Page<UserPrizeItem> findByUser_UserId(Integer userUserId, Pageable pageable);

    List<UserPrizeItem> findByUser_UserIdAndClaimed(Integer userUserId, boolean claimed);


}
