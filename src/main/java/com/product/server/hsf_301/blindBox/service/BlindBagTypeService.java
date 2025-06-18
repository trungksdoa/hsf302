package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BlindBagTypeService {
    Page<PackagesBox> getAllBlindBagTypes(int page, int size);
    List<PackagesBox> getAllBlindBagTypes();
    PackagesBox getBlindBagTypeById(Integer id);
    
    PackagesBox saveBlindBagType(PackagesBox blindBagType, MultipartFile imageFile) throws IOException;
    
    void deleteBlindBagType(Integer id);
    void updateBlindBagType(Integer id, PackagesBox updatedBlindBagType, MultipartFile imageFile) throws IOException;

    List<PackagesBox> getActiveBlindBagTypes();
}
