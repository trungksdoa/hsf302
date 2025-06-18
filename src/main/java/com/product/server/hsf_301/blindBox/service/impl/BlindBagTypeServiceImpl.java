package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.repository.BlindBoxRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlindBagTypeServiceImpl implements BlindBagTypeService {

    private final BlindBoxRepository blindBagTypeRepository;

    @Autowired
    public BlindBagTypeServiceImpl(BlindBoxRepository blindBagTypeRepository) {
        this.blindBagTypeRepository = blindBagTypeRepository;
    }

    @Override
    public Page<BlindPackage> getAllBlindBagTypes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blindBagTypeRepository.findAll(pageable);
    }

    @Override
    public List<BlindPackage> getAllBlindBagTypes() {
        return blindBagTypeRepository.findAll();
    }

    @Override
    public BlindPackage getBlindBagTypeById(Integer id) {
        return blindBagTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlindBagType not found with id: " + id));
    }

    @Override
    public BlindPackage saveBlindBagType(BlindPackage blindBagType, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            blindBagType.setImageData(imageFile.getBytes());
            blindBagType.setImageType(imageFile.getContentType());
        }
        return blindBagTypeRepository.save(blindBagType);
    }

    @Override
    public void deleteBlindBagType(Integer id) {
        blindBagTypeRepository.deleteById(id);
    }

    @Override
    public List<BlindPackage> getActiveBlindBagTypes() {
        return blindBagTypeRepository.findAll().stream()
                .filter(BlindPackage::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public void updateBlindBagType(Integer id, BlindPackage updatedBlindBagType, MultipartFile imageFile) throws IOException {
        BlindPackage existing = getBlindBagTypeById(id);

        if (existing == null) {
            throw new RuntimeException("BlindBagType not found with id: " + id);
        }

        // Cập nhật tên
        existing.setName(updatedBlindBagType.getName());

        // Cập nhật giá mỗi lượt quay
        existing.setPricePerSpin(updatedBlindBagType.getPricePerSpin());

        // Cập nhật mô tả
        existing.setDescription(updatedBlindBagType.getDescription());

        // Nếu có ảnh mới thì cập nhật ảnh
        if (imageFile != null && !imageFile.isEmpty()) {
            existing.setImageData(imageFile.getBytes());
            existing.setImageType(imageFile.getContentType());
        }

        // Lưu lại object đã cập nhật
        blindBagTypeRepository.save(existing);
    }
}
