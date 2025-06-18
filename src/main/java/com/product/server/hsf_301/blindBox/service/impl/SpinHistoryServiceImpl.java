package com.product.server.hsf_301.blindBox.service.impl;


import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.blindBox.model.User;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import com.product.server.hsf_301.blindBox.repository.SpinHistoryRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import com.product.server.hsf_301.blindBox.service.SpinHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SpinHistoryServiceImpl implements SpinHistoryService {

    private final SpinHistoryRepository spinHistoryRepository;
    private final BlindBagTypeService blindBagTypeService;
    private final PrizeItemService prizeItemService;
    private final PrizeItemRepository prizeItemRepository;
    private final Random random = new Random();
    
    // In a real app, you would inject a UserService here
    
    @Autowired
    public SpinHistoryServiceImpl(
            SpinHistoryRepository spinHistoryRepository,
            BlindBagTypeService blindBagTypeService,
            PrizeItemService prizeItemService, PrizeItemRepository prizeItemRepository) {
        this.spinHistoryRepository = spinHistoryRepository;
        this.blindBagTypeService = blindBagTypeService;
        this.prizeItemService = prizeItemService;
        this.prizeItemRepository = prizeItemRepository;
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
    @Override
    public SpinHistory spin(User user, BlindPackage blindPackage) {
        try {
            // Get active prize items for the blind package
            List<PrizeItem> availableItems = prizeItemRepository
                    .findByBlindBagTypeAndIsActiveTrue(blindPackage);

            if (availableItems.isEmpty()) {
                return saveSpinHistory(user, blindPackage, null, false,
                        "No available items in this blind package");
            }

            // Validate probabilities sum to 1
            double totalProbability = availableItems.stream()
                    .mapToDouble(PrizeItem::getProbability)
                    .sum();

            if (Math.abs(totalProbability - 1.0) > 0.0001) {
                return saveSpinHistory(user, blindPackage, null, false,
                        "Invalid probability distribution");
            }

            // Select prize based on probability
            PrizeItem selectedPrize = selectRandomPrize(availableItems);

            if (selectedPrize != null) {
                return saveSpinHistory(user, blindPackage, selectedPrize, true, null);
            }

            return saveSpinHistory(user, blindPackage, null, false,
                    "Failed to select prize item");

        } catch (Exception e) {
            return saveSpinHistory(user, blindPackage, null, false,
                    "System error: " + e.getMessage());
        }
    }
    @Override
    public List<SpinHistory> spinMultiple(User user, BlindPackage blindPackage, int count) {
        List<SpinHistory> results = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            SpinHistory result = spin(user, blindPackage);
            results.add(result);
        }

        return results;
    }
    private PrizeItem selectRandomPrize(List<PrizeItem> availableItems) {
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;

        for (PrizeItem item : availableItems) {
            cumulativeProbability += item.getProbability();
            if (randomValue <= cumulativeProbability) {
                return item;
            }
        }

        return null;
    }

    private SpinHistory saveSpinHistory(User user, BlindPackage blindPackage,
                                        PrizeItem prizeItem, boolean success, String errorMessage) {
        SpinHistory history = new SpinHistory();
        history.setUser(user);
        history.setBlindBagId(blindPackage);
        history.setPrizeItemId(prizeItem);
        history.setPrice(blindPackage.getPricePerSpin().doubleValue());
        history.setSuccess(success);
        history.setRedeemed(false);
        history.setSpinTime(LocalDateTime.now());
        history.setErrorMessage(errorMessage);

        return spinHistoryRepository.save(history);
    }

}
