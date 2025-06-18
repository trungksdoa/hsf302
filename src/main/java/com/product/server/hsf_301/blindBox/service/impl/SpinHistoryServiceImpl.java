package com.product.server.hsf_301.blindBox.service.impl;


import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.repository.SpinHistoryRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.OrderService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import com.product.server.hsf_301.blindBox.service.SpinHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpinHistoryServiceImpl implements SpinHistoryService {

    private final SpinHistoryRepository spinHistoryRepository;
    private final BlindBagTypeService blindBagTypeService;
    private final PrizeItemService prizeItemService;
    private final OrderService orderService;
    // In a real app, you would inject a UserService here
    


    @Override
    public List<SpinHistory> getAllSpinHistory() {
        return spinHistoryRepository.findAll();
    }

    @Override
    public SpinHistory getSpinHistoryById(Integer id) {
        return spinHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SpinHistory not found with id: " + id));
    }

    @Override
    public Page<SpinHistory> getSpinHistoryByUser(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("spinTime").descending());

        // Gọi repository kèm pageable
        return spinHistoryRepository.findByUser(user, pageable);

    }

    @Override
    public SpinHistory saveSpinHistory(SpinHistory spinHistory) {
        return spinHistoryRepository.save(spinHistory);
    }

    @Override
    public SpinHistory redeemPrize(Integer spinId) {
        SpinHistory spinHistory = getSpinHistoryById(spinId);
        
        if (spinHistory.isRedeemed()) {
            throw new RuntimeException("Prize already redeemed");
        }

        if(!spinHistory.getPrizeItemId().isClaimAble()){
            return spinHistory;
        }

        orderService.saveOrder(spinHistory );
        
        spinHistory.setRedeemed(true);
        spinHistory.setRedeemedAt(LocalDateTime.now());
        
        return spinHistoryRepository.save(spinHistory);
    }


    @Override
    public List<SpinHistory> redeemPrizes(List<Integer> spinIds) {
        List<SpinHistory> redeemedSpinHistories = new ArrayList<>();
        List<SpinHistory> claimableSpinHistories = new ArrayList<>();

        // Lọc và validate tất cả SpinHistory trước
        for (Integer spinId : spinIds) {
            SpinHistory spinHistory = getSpinHistoryById(spinId);

            if (spinHistory.isRedeemed()) {
                throw new RuntimeException("Prize with ID " + spinId + " already redeemed");
            }

            if (spinHistory.getPrizeItemId().isClaimAble()) {
                claimableSpinHistories.add(spinHistory);
            }

            redeemedSpinHistories.add(spinHistory);
        }

        // Tạo order cho tất cả claimable items
        if (!claimableSpinHistories.isEmpty()) {
            orderService.saveOrder(claimableSpinHistories);
        }

        // Cập nhật trạng thái redeemed cho tất cả
        for (SpinHistory spinHistory : redeemedSpinHistories) {
            if (spinHistory.getPrizeItemId().isClaimAble()) {
                spinHistory.setRedeemed(true);
                spinHistory.setRedeemedAt(LocalDateTime.now());
                spinHistoryRepository.save(spinHistory);
            }
        }

        return redeemedSpinHistories;
    }

    @Override
    public SpinHistory spin(Integer userId, Integer bagTypeId) {
        // In a real app, you would get the user from a UserService
        User user = new User();
        user.setUserId(userId);
        
        BlindPackage blindBagType = blindBagTypeService.getBlindBagTypeById(bagTypeId);
        PrizeItem prizeItem = prizeItemService.getRandomPrizeByBagType(bagTypeId);
        
        SpinHistory spinHistory = new SpinHistory();
        spinHistory.setUser(user);
        spinHistory.setBlindBagId(blindBagType);
        spinHistory.setPrizeItemId(prizeItem);
        spinHistory.setSpinTime(LocalDateTime.now());
        
        return spinHistoryRepository.save(spinHistory);
    }
}
