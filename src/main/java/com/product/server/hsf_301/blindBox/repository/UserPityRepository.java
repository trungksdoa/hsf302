package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.UserPityStatus;
import com.product.server.hsf_301.user.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserPityRepository extends JpaRepository<UserPityStatus, Long> {
    @Query("select u from UserPityStatus u where u.appUser = ?1 and u.packagesBox = ?2")
    UserPityStatus findByAppUserAndPackagesBox(AppUser appUser, PackagesBox packagesBox);
}
