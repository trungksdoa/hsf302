package com.product.server.hsf_301.blindBox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrizeItemDisplayDTO {
    private Integer itemId;
    private String itemName;
    private RareEnum rarity;
    private Double baseProbability;
    private Double pityBonus;
    private Double currentProbability;
    private String displayString; // "5.00% + 1.00% = 6.00%"
    private String imageUrl;
    private String description;
}