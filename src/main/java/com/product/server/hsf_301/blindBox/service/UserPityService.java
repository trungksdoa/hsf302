package com.product.server.hsf_301.blindBox.service;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.UserPityStatus;
import com.product.server.hsf_301.user.model.AppUser;

import java.util.List;

public interface UserPityService {
    UserPityStatus findUserPityStatusById(Long id);
    UserPityStatus findUserPityStatusByUser(AppUser user,PackagesBox packagesBox);

    UserPityStatus addPity(AppUser appUser, PackagesBox packagesBox);

    void updatePity(UserPityStatus userPityStatus);
    
    // New methods
    List<UserPityStatus> getAllPityStatuses();
    UserPityStatus resetPity(AppUser user,PackagesBox packagesBox);
}
