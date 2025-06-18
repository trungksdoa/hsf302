package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.Collection;
import com.product.server.hsf_301.blindBox.model.AppUser;
import com.product.server.hsf_301.blindBox.model.PackagesBox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    List<Collection> findByUser(AppUser user);
    Page<Collection> findByUser(AppUser user, Pageable pageable);
    Optional<Collection> findByUserAndBlindPackage(AppUser user, PackagesBox blindPackage);
    boolean existsByUserAndBlindPackage(AppUser user, PackagesBox blindPackage);
}
