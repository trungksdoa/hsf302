package com.product.server.hsf_301.blindBox.service.impl;


import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.blindBox.model.User;
import com.product.server.hsf_301.blindBox.repository.SpinHistoryRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import com.product.server.hsf_301.blindBox.service.SpinHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpinHistoryServiceImpl implements SpinHistoryService {

    private final SpinHistoryRepository spinHistoryRepository;
    private final BlindBagTypeService blindBagTypeService;
    private final PrizeItemService prizeItemService;
    
    // In a real app, you would inject a UserService here
    
    @Autowired
    public SpinHistoryServiceImpl(
            SpinHistoryRepository spinHistoryRepository,
            BlindBagTypeService blindBagTypeService,
            PrizeItemService prizeItemService) {
        this.spinHistoryRepository = spinHistoryRepository;
        this.blindBagTypeService = blindBagTypeService;
        this.prizeItemService = prizeItemService;
    }

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
    public List<SpinHistory> getSpinHistoryByUser(User user) {
        return spinHistoryRepository.findByUser(user);
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
        
        spinHistory.setRedeemed(true);
        spinHistory.setRedeemedAt(LocalDateTime.now());
        
        return spinHistoryRepository.save(spinHistory);
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
