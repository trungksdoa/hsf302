package com.product.server.hsf_301.blindBox.service.impl;


import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import com.product.server.hsf_301.blindBox.repository.SpinHistoryRepository;
import com.product.server.hsf_301.blindBox.service.SpinHistoryService;
import com.product.server.hsf_301.blindBox.service.UserPityService;
import com.product.server.hsf_301.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SpinHistoryServiceImpl implements SpinHistoryService {

    private final SpinHistoryRepository spinHistoryRepository;
    private final PrizeItemRepository prizeItemRepository;
    private final Random random = new Random();
    private final UserPityService userPityService;
    // In a real app, you would inject a UserService here
    private final Double baseRare = 0.5;

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
    public Page<SpinHistory> getSpinHistoryByUser(AppUser user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("spinTime").descending());

        // Gọi repository kèm pageable
        return spinHistoryRepository.findByUser(user, pageable);

    }

    @Override
    public List<SpinHistory> getSpinHistoryByUser(AppUser user) {
        return spinHistoryRepository.findSpinHistoriesByUser(user);
    }

    @Override
    public SpinHistory saveSpinHistory(SpinHistory spinHistory) {
        return spinHistoryRepository.save(spinHistory);
    }

    @Override
    public SpinHistory redeemPrize(Integer spinId) {
        SpinHistory spinHistory = getSpinHistoryById(spinId);

        if (!spinHistory.getPrizeItemId().isClaimAble()) {
            return spinHistory;
        }


        return spinHistoryRepository.save(spinHistory);
    }


    @Override
    public List<SpinHistory> redeemPrizes(List<Integer> spinIds) {
        List<SpinHistory> redeemedSpinHistories = new ArrayList<>();

        // Lọc và validate tất cả SpinHistory trước
        for (Integer spinId : spinIds) {
            SpinHistory spinHistory = getSpinHistoryById(spinId);
            redeemedSpinHistories.add(spinHistory);
        }


        // Cập nhật trạng thái redeemed cho tất cả
        for (SpinHistory spinHistory : redeemedSpinHistories) {
            if (spinHistory.getPrizeItemId().isClaimAble()) {
                spinHistoryRepository.save(spinHistory);
            }
        }

        return redeemedSpinHistories;
    }

    @Override
    public SpinHistory spin(Integer userId, Integer bagTypeId) {
//        // In a real app, you would get the user from a UserService
//        AppUser user = new AppUser();
//        user.setUserId(userId);
//
//        PackagesBox blindBagType = blindBagTypeService.getBlindBagTypeById(bagTypeId);
//        PrizeItem prizeItem = prizeItemService.getRandomPrizeByBagType(bagTypeId);
//
//        SpinHistory spinHistory = new SpinHistory();
//        spinHistory.setUser(user);
//        spinHistory.setBlindBagId(blindBagType);
//        spinHistory.setPrizeItemId(prizeItem);
//        spinHistory.setSpinTime(LocalDateTime.now());
//
//        return spinHistoryRepository.save(spinHistory);
        throw new RuntimeException("Not supported function");
    }

    @Override
    @Transactional
    public SpinHistory spin(AppUser user, PackagesBox blindPackage) {
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
            UserPityStatus userPityStatus = userPityService.findUserPityStatusByUser(user, blindPackage);
            if (userPityStatus == null) {
                userPityStatus = userPityService.addPity(user, blindPackage);
            }

            PrizeItem selectedPrize = new PrizeItem();

            //Tăng count để set
            userPityStatus.setCount(userPityStatus.getCount() + 1);

            if (userPityStatus.getCurrent_pity() == 99) {
                Optional<PrizeItem> prizeItem = availableItems.stream().filter(res -> res.getRarity().equals(RareEnum.SPECIAL)).findAny();
                if (prizeItem.isEmpty()) {
                    throw new RuntimeException("selected item is not available");
                }

                selectedPrize = prizeItem.get();
                userPityService.resetPity(user,blindPackage);
                return saveSpinHistory(user, blindPackage, selectedPrize, true, null);

            }

            // Select prize based on probability
            selectedPrize = selectRandomPrize(availableItems, userPityStatus);

            if (selectedPrize == null) {
                throw new RuntimeException("selected item is not available");
            }


            userPityService.updatePity(userPityStatus);
            // Check if it's GOOD_LUCK (no prize)
            if (selectedPrize.getRarity() == RareEnum.GOOD_LUCK) {
                // GOOD_LUCK means "better luck next time" - no actual prize
                return saveSpinHistory(user, blindPackage, null, false, "Better luck next time!");
            }

            if(selectedPrize.getRarity() == RareEnum.SPECIAL){
                userPityService.resetPity(user,blindPackage);
            }


            // Deactivate selected prize and redistribute probabilities
//            deactivateAndRedistribute(selectedPrize, availableItems);

            return saveSpinHistory(user, blindPackage, selectedPrize, true, null);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<SpinHistory> spinMultiple(AppUser user, PackagesBox blindPackage, int count) {
        List<SpinHistory> results = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            SpinHistory result = spin(user, blindPackage);
            results.add(result);
        }

        return results;
    }

    private PrizeItem selectRandomPrize(List<PrizeItem> availableItems, UserPityStatus userPityStatus) {
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

    private SpinHistory saveSpinHistory(AppUser user, PackagesBox blindPackage,
                                        PrizeItem prizeItem, boolean success, String errorMessage) {
        SpinHistory history = new SpinHistory();
        history.setUser(user);
        history.setBlindBagId(blindPackage);
        history.setPrizeItemId(prizeItem); // null for GOOD_LUCK
        history.setPrice(blindPackage.getPricePerSpin().doubleValue());
        history.setSuccess(success);
        history.setSpinTime(LocalDateTime.now());


        return spinHistoryRepository.save(history);
    }

    private void Redistribute(PrizeItem selectedPrize, List<PrizeItem> availableItems, UserPityStatus userPityStatus) {
        try {
            // Get selected prize probability
            double selectedProbability = selectedPrize.getProbability();

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
