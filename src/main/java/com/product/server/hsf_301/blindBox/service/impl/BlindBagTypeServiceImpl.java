package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.BlindBagType;
import com.product.server.hsf_301.blindBox.repository.BlindBoxRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


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
    public Page<BlindBagType> getAllBlindBagTypes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return blindBagTypeRepository.findAll(pageable);
    }

    @Override
    public List<BlindBagType> getAllBlindBagTypes() {
        return blindBagTypeRepository.findAll();
    }

    @Override
    public BlindBagType getBlindBagTypeById(Integer id) {
        return blindBagTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BlindBagType not found with id: " + id));
    }

    @Override
    public BlindBagType saveBlindBagType(BlindBagType blindBagType) {
        return blindBagTypeRepository.save(blindBagType);
    }

    @Override
    public void deleteBlindBagType(Integer id) {
        blindBagTypeRepository.deleteById(id);
    }

    @Override
    public List<BlindBagType> getActiveBlindBagTypes() {
        return blindBagTypeRepository.findAll().stream()
                .filter(BlindBagType::isActive)
                .collect(Collectors.toList());
    }
}
