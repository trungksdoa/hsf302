package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.Collection;
import com.product.server.hsf_301.blindBox.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {
    List<Collection> findByUser(User user);
    Page<Collection> findByUser(User user, Pageable pageable);
    Optional<Collection> findByUserAndBlindPackage(User user, com.product.server.hsf_301.blindBox.model.BlindPackage blindPackage);
    boolean existsByUserAndBlindPackage(User user, com.product.server.hsf_301.blindBox.model.BlindPackage blindPackage);
}
