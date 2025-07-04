package com.product.server.hsf_301.blindBox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private String imageUrl; // Ensure this field exists

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    private String imageType;
    
    @Column(name = "rarity", nullable = false, length = 20)
    private RareEnum rarity;
    
    @ManyToOne
    @JoinColumn(name = "bag_type_id")
    @JsonIgnore
    private PackagesBox blindBagType;
    
    @Column(name = "probability", nullable = false)
    private Double probability;

    private boolean isActive = true;

    @Column(name = "is_claim_able")
    private boolean isClaimAble = false;
    
    @Column(name = "description")
    private String description;
}