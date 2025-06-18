package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.*;

import com.product.server.hsf_301.blindBox.model.AppUser;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SpinHistoryService {
    List<SpinHistory> getAllSpinHistory();
    
    SpinHistory getSpinHistoryById(Integer id);
    
    Page<SpinHistory> getSpinHistoryByUser(AppUser user, int page, int size);
    
    SpinHistory saveSpinHistory(SpinHistory spinHistory);
    
    SpinHistory redeemPrize(Integer spinId);

    List<SpinHistory> redeemPrizes(List<Integer> spins);

    SpinHistory spin(Integer userId, Integer bagTypeId);
    List<SpinHistory> spinMultiple(AppUser user, PackagesBox blindPackage, int count);
    SpinHistory spin(AppUser user, PackagesBox blindPackage);

}
