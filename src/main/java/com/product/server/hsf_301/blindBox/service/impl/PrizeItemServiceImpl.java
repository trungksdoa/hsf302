package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.PrizeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PrizeItemServiceImpl implements PrizeItemService {

    private final PrizeItemRepository prizeItemRepository;
    private final BlindBagTypeService blindBagTypeService;
    private final Random random = new Random();

    @Autowired
    public PrizeItemServiceImpl(PrizeItemRepository prizeItemRepository, BlindBagTypeService blindBagTypeService) {
        this.prizeItemRepository = prizeItemRepository;
        this.blindBagTypeService = blindBagTypeService;
    }

    @Override
    public List<PrizeItem> getAllPrizeItems() {
        return prizeItemRepository.findAll();
    }

    @Override
    public PrizeItem getPrizeItemById(Integer id) {
        return prizeItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PrizeItem not found with id: " + id));
    }

    @Override
    public PrizeItem savePrizeItem(PrizeItem prizeItem) {
        return prizeItemRepository.save(prizeItem);
    }

    @Override
    public void deletePrizeItem(Integer id) {
        prizeItemRepository.deleteById(id);
    }

    @Override
    public List<PrizeItem> getPrizeItemsByBlindBagType(BlindPackage blindBagType) {
        return prizeItemRepository.findByBlindBagType(blindBagType);
    }

    @Override
    public List<PrizeItem> getPrizeItemByBlindBoxAndActive(BlindPackage blindBagType) {
        return prizeItemRepository.findByBlindBagTypeAndIsActiveTrue(blindBagType);
    }

    @Override
    public List<PrizeItem> getActivePrizeItems() {
        return prizeItemRepository.findByIsActiveTrue();
    }

    @Override
    public PrizeItem getRandomPrizeByBagType(Integer bagTypeId) {
        BlindPackage blindBagType = blindBagTypeService.getBlindBagTypeById(bagTypeId);
        List<PrizeItem> activePrizes = prizeItemRepository.findByBlindBagTypeAndIsActiveTrue(blindBagType);
        
        if (activePrizes.isEmpty()) {
            throw new RuntimeException("No active prizes found for this bag type");
        }
        
        // Create weighted probability list
        List<PrizeItem> weightedPrizes = new ArrayList<>();
        for (PrizeItem prize : activePrizes) {
            // Convert probability to number of entries (multiply by 1000 to handle decimal probabilities)
            int entries = (int) (prize.getProbability() * 1000);
            for (int i = 0; i < entries; i++) {
                weightedPrizes.add(prize);
            }
        }
        
        // Get random prize
        int randomIndex = random.nextInt(weightedPrizes.size());
        return weightedPrizes.get(randomIndex);
    }
}
