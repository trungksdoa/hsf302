package com.product.server.hsf_301.blindBox.service.impl;


import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import com.product.server.hsf_301.blindBox.repository.SpinHistoryRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.OrderService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import com.product.server.hsf_301.blindBox.service.SpinHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SpinHistoryServiceImpl implements SpinHistoryService {

    private final SpinHistoryRepository spinHistoryRepository;
    private final BlindBagTypeService blindBagTypeService;
    private final PrizeItemService prizeItemService;
    private final OrderService orderService;
    private final PrizeItemRepository prizeItemRepository;
    private final Random random = new Random();

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

    @Override
    public SpinHistory spin(User user, BlindPackage blindPackage) {
        try {
            // Get active prize items for the blind package
            List<PrizeItem> availableItems = prizeItemRepository
                    .findByBlindBagTypeAndIsActiveTrue(blindPackage);

            if (availableItems.isEmpty()) {
                throw new RuntimeException("No available items in this blind package");
            }

            // Validate probabilities sum to 1
            double totalProbability = availableItems.stream()
                    .mapToDouble(PrizeItem::getProbability)
                    .sum();

            if (Math.abs(totalProbability - 1.0) > 0.0001) {
                throw new RuntimeException("Invalid probability distribution");
            }

            // Select prize based on probability
            PrizeItem selectedPrize = selectRandomPrize(availableItems);

            if(selectedPrize == null ){
                throw new RuntimeException("selected item is not available");
            }

            // Check if it's GOOD_LUCK (no prize)
            if (selectedPrize.getRarity() == RareEnum.GOOD_LUCK) {
                // GOOD_LUCK means "better luck next time" - no actual prize
                return saveSpinHistory(user, blindPackage, null, false, "Better luck next time!");
            }

            // Deactivate selected prize and redistribute probabilities
            deactivateAndRedistribute(selectedPrize, availableItems);

            return saveSpinHistory(user, blindPackage, selectedPrize, true, null);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
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
            if (item.isActive() && randomValue <= cumulativeProbability) {
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
        history.setPrizeItemId(prizeItem); // null for GOOD_LUCK
        history.setPrice(blindPackage.getPricePerSpin().doubleValue());
        history.setSuccess(success);
        history.setRedeemed(false);
        history.setSpinTime(LocalDateTime.now());


        return spinHistoryRepository.save(history);
    }

    private void deactivateAndRedistribute(PrizeItem selectedPrize, List<PrizeItem> availableItems) {
        try {
            // Get selected prize probability
            double selectedProbability = selectedPrize.getProbability();
            
            // Deactivate selected prize
            selectedPrize.setActive(false);
            prizeItemRepository.save(selectedPrize);
            
            // Get remaining active items (excluding selected one)
            List<PrizeItem> remainingItems = availableItems.stream()
                    .filter(item -> !item.getItemId().equals(selectedPrize.getItemId()))
                    .toList();
            
            if (remainingItems.isEmpty()) {
                return;
            }
            
            // Calculate total probability of remaining items  
            double totalRemaining = remainingItems.stream()
                    .mapToDouble(PrizeItem::getProbability)
                    .sum();
            
            // Redistribute selected prize's probability proportionally
            for (PrizeItem item : remainingItems) {
                double proportion = item.getProbability() / totalRemaining;
                double additionalProbability = selectedProbability * proportion;
                double newProbability = item.getProbability() + additionalProbability;
                
                item.setProbability(newProbability);
                prizeItemRepository.save(item);
            }
            
            System.out.println("Deactivated: " + selectedPrize.getItemName() + 
                    " (" + (selectedProbability * 100) + "%)");
            System.out.println("Redistributed to " + remainingItems.size() + " remaining items");
            
        } catch (Exception e) {
            System.err.println("Error redistributing probabilities: " + e.getMessage());
        }
    }

}
