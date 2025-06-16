package com.product.server.hsf_301.blindBox.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Prize_Items")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrizeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer itemId;
    
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;
    
    @Column(name = "item_image")
    private String itemImage;
    
    @Column(name = "rarity", nullable = false, length = 20)
    private RareEnum rarity;
    
    @ManyToOne
    @JoinColumn(name = "bag_type_id")
    private BlindPackage blindBagType;
    
    @Column(name = "probability", nullable = false)
    private Double probability;

    private boolean isActive = true;

    private boolean isClaimAble = false;
}