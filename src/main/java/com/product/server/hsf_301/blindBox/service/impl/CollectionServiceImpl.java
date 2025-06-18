package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.Collection;
import com.product.server.hsf_301.blindBox.model.AppUser;
import com.product.server.hsf_301.blindBox.repository.CollectionRepository;
import com.product.server.hsf_301.blindBox.service.BlindBagTypeService;
import com.product.server.hsf_301.blindBox.service.CollectionService;
import com.product.server.hsf_301.blindBox.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserService userService;
    private final BlindBagTypeService blindBagTypeService;

    @Override
    public Collection addToCollection(Integer userId, Integer blindPackageId) {
        AppUser user = userService.getUserById(userId);
        PackagesBox blindPackage = blindBagTypeService.getBlindBagTypeById(blindPackageId);
        
        // Check if already in collection
        if (collectionRepository.existsByUserAndBlindPackage(user, blindPackage)) {
            throw new RuntimeException("Item already in collection");
        }
        
        Collection collection = new Collection();
        collection.setUser(user);
        collection.setBlindPackage(blindPackage);
        
        return collectionRepository.save(collection);
    }

    @Override
    public List<Collection> getUserCollection(AppUser user) {
        return collectionRepository.findByUser(user);
    }

    @Override
    public Page<Collection> getUserCollection(AppUser user, Pageable pageable) {
        return collectionRepository.findByUser(user, pageable);
    }

    @Override
    public boolean isInCollection(Integer userId, Integer blindPackageId) {
        AppUser user = userService.getUserById(userId);
        PackagesBox blindPackage = blindBagTypeService.getBlindBagTypeById(blindPackageId);
        return collectionRepository.existsByUserAndBlindPackage(user, blindPackage);
    }

    @Override
    public void removeFromCollection(Integer userId, Integer blindPackageId) {
        AppUser user = userService.getUserById(userId);
        PackagesBox blindPackage = blindBagTypeService.getBlindBagTypeById(blindPackageId);
        
        collectionRepository.findByUserAndBlindPackage(user, blindPackage)
            .ifPresent(collectionRepository::delete);
    }
}
