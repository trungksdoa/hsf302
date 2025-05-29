package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.BlindBagType;
import com.product.server.hsf_301.blindBox.model.PrizeItem;

import java.util.List;

public interface PrizeItemService {
    List<PrizeItem> getAllPrizeItems();
    
    PrizeItem getPrizeItemById(Integer id);
    
    PrizeItem savePrizeItem(PrizeItem prizeItem);
    
    void deletePrizeItem(Integer id);
    
    List<PrizeItem> getPrizeItemsByBlindBagType(BlindBagType blindBagType);
    List<PrizeItem> getPrizeItemByBlindBoxAndActive(BlindBagType blindBagType);
    List<PrizeItem> getActivePrizeItems();
    
    PrizeItem getRandomPrizeByBagType(Integer bagTypeId);
}
