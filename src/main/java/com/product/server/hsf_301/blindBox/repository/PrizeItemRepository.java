package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrizeItemRepository extends JpaRepository<PrizeItem, Integer> {
    List<PrizeItem> findByBlindBagType(PackagesBox blindBagType);
    
    List<PrizeItem> findByIsActiveTrue();
    
    List<PrizeItem> findByBlindBagTypeAndIsActiveTrue(PackagesBox blindPackage);

    List<PrizeItem> findAllByBlindBagType_Id(Integer blindBagTypeId);
}
