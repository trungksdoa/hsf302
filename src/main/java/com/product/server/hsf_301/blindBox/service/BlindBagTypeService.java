package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.BlindBagType;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BlindBagTypeService {
    Page<BlindBagType> getAllBlindBagTypes(int page, int size);
    List<BlindBagType> getAllBlindBagTypes();
    BlindBagType getBlindBagTypeById(Integer id);
    
    BlindBagType saveBlindBagType(BlindBagType blindBagType);
    
    void deleteBlindBagType(Integer id);
    
    List<BlindBagType> getActiveBlindBagTypes();
}
