package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.PrizeItem;

import java.util.List;

public interface PrizeItemService {
    List<PrizeItem> getAllPrizeItems();
    List<PrizeItem> getAllPrizeItemsByBlindId(int id);
    PrizeItem getPrizeItemById(Integer id);
    
    PrizeItem savePrizeItem(PrizeItem prizeItem);
    public PrizeItem updateItem(Integer id, PrizeItem updatedItem);
    void deletePrizeItem(Integer id);
    
    List<PrizeItem> getPrizeItemsByBlindBagType(BlindPackage blindBagType);
    List<PrizeItem> getPrizeItemByBlindBoxAndActive(BlindPackage blindBagType);
    List<PrizeItem> getActivePrizeItems();
    
    PrizeItem getRandomPrizeByBagType(Integer bagTypeId);
}
