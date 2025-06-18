package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.Collection;
import com.product.server.hsf_301.blindBox.model.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CollectionService {
    Collection addToCollection(Integer userId, Integer blindPackageId);
    List<Collection> getUserCollection(AppUser user);
    Page<Collection> getUserCollection(AppUser user, Pageable pageable);
    boolean isInCollection(Integer userId, Integer blindPackageId);
    void removeFromCollection(Integer userId, Integer blindPackageId);
}
