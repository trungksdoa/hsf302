package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.model.SpinHistory;
import com.product.server.hsf_301.blindBox.model.User;

import java.util.List;

public interface SpinHistoryService {
    List<SpinHistory> getAllSpinHistory();
    
    SpinHistory getSpinHistoryById(Integer id);
    
    List<SpinHistory> getSpinHistoryByUser(User user);
    
    SpinHistory saveSpinHistory(SpinHistory spinHistory);
    
    SpinHistory redeemPrize(Integer spinId);
    
    SpinHistory spin(Integer userId, Integer bagTypeId);
    List<SpinHistory> spinMultiple(User user, BlindPackage blindPackage, int count);
    SpinHistory spin(User user, BlindPackage blindPackage);

}
