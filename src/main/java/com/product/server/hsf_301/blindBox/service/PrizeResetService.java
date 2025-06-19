package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.model.RareEnum;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class PrizeResetService {

    private final PrizeItemRepository prizeItemRepository;

    /**
     * Standard probability distribution based on rarity
     */
    private static final Map<RareEnum, Double> STANDARD_RARITY_PROBABILITIES = Map.of(
            RareEnum.GOOD_LUCK, 0.40,      // 40% - Better luck next time
            RareEnum.COMMON, 0.35,         // 35% - Common items
            RareEnum.UNCOMMON, 0.15,       // 15% - Uncommon items
            RareEnum.RARE, 0.07,           // 7%  - Rare items
            RareEnum.SPECIAL, 0.025        // 2.5% - Special items
    );

    /**
     * Reset all PrizeItems to active = true every day at midnight
     * Also rebalances probabilities to ensure fair distribution
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void resetDailyPrizes() {
        try {
            log.info("Starting daily prize reset with probability rebalancing at: {}", LocalDateTime.now());

            List<PrizeItem> allPrizes = prizeItemRepository.findAll();

            // Reactivate inactive prizes
            int resetCount = 0;
            for (PrizeItem prize : allPrizes) {
                if (!prize.isActive()) {
                    prize.setActive(true);
                    resetCount++;
                }
            }

            // Rebalance probabilities for all items
            rebalanceProbabilities(allPrizes);

            // Save all changes
            prizeItemRepository.saveAll(allPrizes);

            log.info("Successfully reset {} inactive prizes and rebalanced probabilities", resetCount);

        } catch (Exception e) {
            log.error("Error occurred during daily prize reset: {}", e.getMessage(), e);
        }
    }

    /**
     * Manual reset method for admin use with probability rebalancing
     */
    @Transactional
    public int manualResetPrizes() {
        try {
            log.info("Manual prize reset with probability rebalancing initiated at: {}", LocalDateTime.now());

            List<PrizeItem> allPrizes = prizeItemRepository.findAll();

            // Reactivate inactive prizes
            int resetCount = 0;
            for (PrizeItem prize : allPrizes) {
                if (!prize.isActive()) {
                    prize.setActive(true);
                    resetCount++;
                }
            }

            // Rebalance probabilities
            rebalanceProbabilities(allPrizes);

            // Save changes
            prizeItemRepository.saveAll(allPrizes);

            log.info("Manual reset completed: {} prizes activated and probabilities rebalanced", resetCount);

            return resetCount;

        } catch (Exception e) {
            log.error("Error during manual prize reset: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to reset prizes: " + e.getMessage());
        }
    }

    /**
     * Rebalance probabilities based on rarity
     */
    private void rebalanceProbabilities(List<PrizeItem> allPrizes) {
        // Group prizes by package and rarity
        Map<PackagesBox, Map<RareEnum, List<PrizeItem>>> packagesByRarity = allPrizes.stream()
                .collect(Collectors.groupingBy(
                        PrizeItem::getBlindBagType,
                        Collectors.groupingBy(PrizeItem::getRarity)
                ));

        // Rebalance each package separately
        for (Map.Entry<PackagesBox, Map<RareEnum, List<PrizeItem>>> packageEntry : packagesByRarity.entrySet()) {
            PackagesBox blindPackage = packageEntry.getKey();
            Map<RareEnum, List<PrizeItem>> rarityGroups = packageEntry.getValue();

            log.info("Rebalancing package: {}", blindPackage.getName());

            // Calculate total intended probability
            double totalIntendedProbability = 0.0;
            for (RareEnum rarity : rarityGroups.keySet()) {
                totalIntendedProbability += STANDARD_RARITY_PROBABILITIES.getOrDefault(rarity, 0.01);
            }

            // Normalization factor to ensure total = 1.0
            double normalizationFactor = 1.0 / totalIntendedProbability;

            log.debug("Total intended probability: {:.6f}, Normalization factor: {:.6f}",
                    totalIntendedProbability, normalizationFactor);

            // Set probabilities for each rarity group with normalization
            for (Map.Entry<RareEnum, List<PrizeItem>> rarityEntry : rarityGroups.entrySet()) {
                RareEnum rarity = rarityEntry.getKey();
                List<PrizeItem> itemsOfRarity = rarityEntry.getValue();

                // Get standard probability and normalize it
                double standardProbability = STANDARD_RARITY_PROBABILITIES.getOrDefault(rarity, 0.01);
                double normalizedRarityProbability = standardProbability * normalizationFactor;

                // Distribute equally among items of same rarity
                double probabilityPerItem = normalizedRarityProbability / itemsOfRarity.size();

                // Set probability for each item
                for (PrizeItem item : itemsOfRarity) {
                    item.setProbability(probabilityPerItem);
                }

                log.debug("Set {:.6f} probability for {} {} items ({:.6f} each)",
                        normalizedRarityProbability, itemsOfRarity.size(), rarity.getValue(), probabilityPerItem);
            }

            // Validate total probability for this package
            List<PrizeItem> packageItems = rarityGroups.values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            validateProbabilityDistribution(packageItems);
        }
    }


    /**
     * Validate probability distribution
     */
    private void validateProbabilityDistribution(List<PrizeItem> items) {
        double totalProbability = items.stream()
                .mapToDouble(PrizeItem::getProbability)
                .sum();

        if (Math.abs(totalProbability - 1.0) > 0.0001) {
            log.warn("Invalid probability distribution detected. Total: {:.6f}", totalProbability);
            throw new RuntimeException(String.format(
                    "Invalid probability distribution. Total: %.6f, Expected: 1.0",
                    totalProbability
            ));
        }

        log.debug("Probability distribution validated. Total: {:.6f}", totalProbability);
    }

    /**
     * Get statistics about current prize status
     */
    public PrizeStatistics getPrizeStatistics() {
        List<PrizeItem> allPrizes = prizeItemRepository.findAll();

        long totalPrizes = allPrizes.size();
        long activePrizes = allPrizes.stream().mapToLong(p -> p.isActive() ? 1 : 0).sum();
        long inactivePrizes = totalPrizes - activePrizes;

        // Get probability stats by rarity
        Map<RareEnum, Double> probabilityByRarity = allPrizes.stream()
                .collect(Collectors.groupingBy(
                        PrizeItem::getRarity,
                        Collectors.summingDouble(PrizeItem::getProbability)
                ));

        return new PrizeStatistics(totalPrizes, activePrizes, inactivePrizes, probabilityByRarity);
    }

    /**
     * Enhanced DTO for prize statistics
     */
    public static class PrizeStatistics {
        private final long totalPrizes;
        private final long activePrizes;
        private final long inactivePrizes;
        private final Map<RareEnum, Double> probabilityByRarity;

        public PrizeStatistics(long totalPrizes, long activePrizes, long inactivePrizes,
                               Map<RareEnum, Double> probabilityByRarity) {
            this.totalPrizes = totalPrizes;
            this.activePrizes = activePrizes;
            this.inactivePrizes = inactivePrizes;
            this.probabilityByRarity = probabilityByRarity;
        }

        public long getTotalPrizes() {
            return totalPrizes;
        }

        public long getActivePrizes() {
            return activePrizes;
        }

        public long getInactivePrizes() {
            return inactivePrizes;
        }

        public Map<RareEnum, Double> getProbabilityByRarity() {
            return probabilityByRarity;
        }

        public double getActivePercentage() {
            return totalPrizes > 0 ? (double) activePrizes / totalPrizes * 100 : 0;
        }

        public double getTotalProbability() {
            return probabilityByRarity.values().stream().mapToDouble(Double::doubleValue).sum();
        }

        public boolean isProbabilityValid() {
            return Math.abs(getTotalProbability() - 1.0) <= 0.0001;
        }
    }
}