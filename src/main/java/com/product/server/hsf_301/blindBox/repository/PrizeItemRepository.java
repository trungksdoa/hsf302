package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrizeItemRepository extends JpaRepository<PrizeItem, Integer> {
    List<PrizeItem> findByBlindBagType(BlindPackage blindBagType);
    
    List<PrizeItem> findByIsActiveTrue();
    
    List<PrizeItem> findByBlindBagTypeAndIsActiveTrue(BlindPackage blindBagType);

    List<PrizeItem> findAllByBlindBagType_Id(Integer blindBagTypeId);
}
