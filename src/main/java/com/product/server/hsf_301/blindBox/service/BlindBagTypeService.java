package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BlindBagTypeService {
    Page<BlindPackage> getAllBlindBagTypes(int page, int size);
    List<BlindPackage> getAllBlindBagTypes();
    BlindPackage getBlindBagTypeById(Integer id);
    
    BlindPackage saveBlindBagType(BlindPackage blindBagType, MultipartFile imageFile) throws IOException;
    
    void deleteBlindBagType(Integer id);
    void updateBlindBagType(Integer id, BlindPackage updatedBlindBagType, MultipartFile imageFile) throws IOException;

    List<BlindPackage> getActiveBlindBagTypes();
}
