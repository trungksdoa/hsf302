package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.*;
import com.product.server.hsf_301.blindBox.repository.BlindBoxRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.UserPityService;
import com.product.server.hsf_301.blindBox.service.UserService;
import com.product.server.hsf_301.user.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlindBagTypeServiceImpl implements BlindBagTypeService {

    private final BlindBoxRepository blindBagTypeRepository;
    private final SpinHistoryServiceImpl spinHistoryService;
    private final UserPityService userPityService;
    private final UserService userService;

    @Autowired
    public BlindBagTypeServiceImpl(BlindBoxRepository blindBagTypeRepository, SpinHistoryServiceImpl spinHistoryService, UserPityService userPityService, UserService userService) {
        this.blindBagTypeRepository = blindBagTypeRepository;
        this.spinHistoryService = spinHistoryService;
        this.userPityService = userPityService;
        this.userService = userService;
    }

    @Override
    public Page<PackagesBoxDisplayDTO> getAllBlindBagTypes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PackagesBox> packagesBoxes = blindBagTypeRepository.findAll(pageable);

        List<PackagesBoxDisplayDTO> result = new ArrayList<>();
        AppUser currentUser = userService.getCurrentUser();

        for (PackagesBox packageBox : packagesBoxes.getContent()) {
            if (!packageBox.isActive()) continue;

            // Tính pity cho user hiện tại và package này
            UserPityStatus userPityStatus = userPityService.findUserPityStatusByUser(currentUser, packageBox);
            double pityBonus = spinHistoryService.calculatePityBonus(userPityStatus != null ? userPityStatus.getCurrent_pity() : 0);

            PackagesBoxDisplayDTO packageDTO = new PackagesBoxDisplayDTO();
            packageDTO.setId(packageBox.getId());
            packageDTO.setName(packageBox.getName());
            packageDTO.setDescription(packageBox.getDescription());
            packageDTO.setUserPity(userPityStatus != null ? userPityStatus.getCurrent_pity() : 0);
            packageDTO.setImageUrl(packageBox.getImageUrl());
            packageDTO.setActive(packageBox.isActive());
            packageDTO.setPricePerSpin(packageBox.getPricePerSpin());



            List<PrizeItemDisplayDTO> prizeItemDTOs = new ArrayList<>();

            for (PrizeItem prizeItem : packageBox.getPrizeItems()) {
                if (!prizeItem.isActive()) continue;

                double baseProbability = prizeItem.getProbability();
                double pityBonusAmount = 0.0;
                double currentProbability = baseProbability;

                // Chỉ áp dụng pity cho rare items
                if (spinHistoryService.isRareItem(prizeItem.getRarity())) {
                    pityBonusAmount = baseProbability * pityBonus;
                    currentProbability = baseProbability + pityBonusAmount;
                }

                PrizeItemDisplayDTO dto = new PrizeItemDisplayDTO();
                dto.setItemId(prizeItem.getItemId());
                dto.setItemName(prizeItem.getItemName());
                dto.setRarity(prizeItem.getRarity());
                dto.setImageUrl(prizeItem.getImageUrl());
                dto.setBaseProbability(baseProbability);
                dto.setPityBonus(pityBonusAmount);
                dto.setCurrentProbability(currentProbability);
                dto.setDisplayString(formatProbabilityDisplay(baseProbability, pityBonusAmount, currentProbability));

                prizeItemDTOs.add(dto);
            }

            packageDTO.setPrizeItems(prizeItemDTOs);
            result.add(packageDTO);
        }

        // Tạo Page mới với content đã được transform
        return new PageImpl<>(result, pageable, packagesBoxes.getTotalElements());
    }

    private String formatProbabilityDisplay(double base, double bonus, double current) {
        if (bonus > 0) {
            return String.format("%.2f%%", current * 100);
        } else {
            return String.format("%.2f%%", base * 100);
        }
    }

    @Override
    public List<PackagesBox> getAllBlindBagTypes() {
        return blindBagTypeRepository.findAll();
    }

    @Override
    public PackagesBox getBlindBagTypeById(Integer id) {
        return blindBagTypeRepository.findById(id).orElseThrow(() -> new RuntimeException("BlindBagType not found with id: " + id));
    }

    @Override
    public PackagesBox saveBlindBagType(PackagesBox blindPackage, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            blindPackage.setImageData(imageFile.getBytes());
            blindPackage.setImageType(imageFile.getContentType());
        }

        blindPackage.getPrizeItems().forEach(res -> {
            res.setBlindBagType(blindPackage);
        });


        return blindBagTypeRepository.save(blindPackage);
    }

    @Override
    public void deleteBlindBagType(Integer id) {
        blindBagTypeRepository.deleteById(id);
    }

    @Override
    public List<PackagesBox> getActiveBlindBagTypes() {
        return blindBagTypeRepository.findAll().stream().filter(PackagesBox::isActive).collect(Collectors.toList());
    }

    @Override
    public void updateBlindBagType(Integer id, PackagesBox updatedBlindBagType, MultipartFile imageFile) throws IOException {
        PackagesBox existing = getBlindBagTypeById(id);

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
