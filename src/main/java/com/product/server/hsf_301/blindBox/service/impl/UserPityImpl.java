package com.product.server.hsf_301.blindBox.service.impl;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.UserPityStatus;
import com.product.server.hsf_301.blindBox.repository.UserPityRepository;
import com.product.server.hsf_301.blindBox.service.UserPityService;
import com.product.server.hsf_301.user.model.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPityImpl implements UserPityService {
    private final UserPityRepository userPityRepository;

    @Override
    public UserPityStatus findUserPityStatusById(Long id) {
        return userPityRepository.findById(id).orElse(null);
    }

    @Override
    public UserPityStatus findUserPityStatusByUser(AppUser user,PackagesBox packagesBox) {
        return userPityRepository.findByAppUserAndPackagesBox(user, packagesBox);
    }

    @Override
    public UserPityStatus addPity(AppUser appUser, PackagesBox packagesBox) {
        UserPityStatus userPityStatus = new UserPityStatus();
        userPityStatus.setAppUser(appUser);
        userPityStatus.setPackagesBox(packagesBox);
        userPityStatus.setCurrent_pity(0);
        return userPityRepository.save(userPityStatus);
    }

    @Override
    public void updatePity(UserPityStatus userPityStatus) {
        userPityStatus.setCurrent_pity(userPityStatus.getCurrent_pity() + 1);
        userPityRepository.save(userPityStatus);
    }
    
    @Override
    public List<UserPityStatus> getAllPityStatuses() {
        return userPityRepository.findAll();
    }
    
    @Override
    public UserPityStatus resetPity(AppUser user,PackagesBox packagesBox) {
        UserPityStatus userPityStatus = userPityRepository.findByAppUserAndPackagesBox(user,packagesBox);
        if (userPityStatus != null) {
            userPityStatus.setCurrent_pity(0);
            return userPityRepository.save(userPityStatus);
        }
        return null;
    }
    

}
