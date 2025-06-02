package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlindBagTypeService {
    Page<BlindPackage> getAllBlindBagTypes(int page, int size);
    List<BlindPackage> getAllBlindBagTypes();
    BlindPackage getBlindBagTypeById(Integer id);
    
    BlindPackage saveBlindBagType(BlindPackage blindBagType);
    
    void deleteBlindBagType(Integer id);
    
    List<BlindPackage> getActiveBlindBagTypes();
}
