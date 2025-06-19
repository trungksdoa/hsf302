package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.UserPrizeItem;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserPrizeItemService {
    Page<UserPrizeItem> getAll(int page, int size);
    List<UserPrizeItem> getAll();
    UserPrizeItem getById(Long id);
    UserPrizeItem save(UserPrizeItem userPrizeItem);
    void deleteById(Long id);

    // Additional useful methods for UserPrizeItem management
    @Transactional(readOnly = true)
    Page<UserPrizeItem> getByUserId(int userId, int page,int size);

    @Transactional(readOnly = true)
    List<UserPrizeItem> getByUserIdAndClaimed(int userId, boolean isClaimed);

    UserPrizeItem claimPrize(Long userPrizeItemId);

    void claimPrize(List<Long> userPrizeItemId);
}
