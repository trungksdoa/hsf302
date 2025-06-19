package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrizeItemRepository extends JpaRepository<PrizeItem, Integer> {
    List<PrizeItem> findByBlindBagType(PackagesBox blindBagType);
    
    List<PrizeItem> findByIsActiveTrue();
    
    List<PrizeItem> findByBlindBagTypeAndIsActiveTrue(PackagesBox blindPackage);

    List<PrizeItem> findAllByBlindBagType_Id(Integer blindBagTypeId);

    // Additional methods for reset functionality
    @Modifying
    @Query("UPDATE PrizeItem p SET p.isActive = true WHERE p.isActive = false")
    int resetAllInactivePrizesToActive();

    @Query("SELECT COUNT(p) FROM PrizeItem p WHERE p.isActive = false")
    long countInactivePrizes();

}
