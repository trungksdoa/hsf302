package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
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
    public List<PrizeItem> getAllPrizeItemsByBlindId(int id) {
        return prizeItemRepository.findAllByBlindBagType_Id(id);
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
    public List<PrizeItem> getPrizeItemsByBlindBagType(PackagesBox blindBagType) {
        return prizeItemRepository.findByBlindBagType(blindBagType);
    }
    @Override
    public PrizeItem updateItem(Integer id, PrizeItem updatedItem) {
        return prizeItemRepository.findById(id).map(existingItem -> {
            existingItem.setItemName(updatedItem.getItemName());
            existingItem.setImageData(updatedItem.getImageData());
            existingItem.setImageType(updatedItem.getImageType());
            existingItem.setRarity(updatedItem.getRarity());
            existingItem.setBlindBagType(updatedItem.getBlindBagType());
            existingItem.setProbability(updatedItem.getProbability());
            existingItem.setActive(updatedItem.isActive());
            return prizeItemRepository.save(existingItem);
        }).orElseThrow(() -> new RuntimeException("PrizeItem not found"));
    }

    @Override
    public List<PrizeItem> getPrizeItemByBlindBoxAndActive(PackagesBox blindBagType) {
        return prizeItemRepository.findByBlindBagTypeAndIsActiveTrue(blindBagType);
    }

    @Override
    public List<PrizeItem> getActivePrizeItems() {
        return prizeItemRepository.findByIsActiveTrue();
    }

    @Override
    public PrizeItem getRandomPrizeByBagType(Integer bagTypeId) {
        PackagesBox blindBagType = blindBagTypeService.getBlindBagTypeById(bagTypeId);
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
