package com.product.server.hsf_301.blindBox.repository;

import com.product.server.hsf_301.blindBox.model.PackagesBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlindBoxRepository extends JpaRepository<PackagesBox, Integer> {
}

