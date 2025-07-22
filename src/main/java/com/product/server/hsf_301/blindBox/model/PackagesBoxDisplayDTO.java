package com.product.server.hsf_301.blindBox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackagesBoxDisplayDTO {
    private int id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal pricePerSpin;
    private int userPity;
    private boolean isActive;
    private List<PrizeItemDisplayDTO> prizeItems;
    // Thêm các field khác từ PackagesBox nếu cần
}