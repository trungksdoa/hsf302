package com.product.server.hsf_301.blindBox;


import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Blind_Bag_Types")
@Data
public class BlindBagType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bag_type_id")
    private Integer bagTypeId;
    
    @Column(name = "type_name", nullable = false, length = 50)
    private String typeName;
    
    @Column(name = "price_per_spin", nullable = false)
    private BigDecimal pricePerSpin;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @OneToMany(mappedBy = "blindBagType")
    private List<PrizeItem> prizeItems;
}