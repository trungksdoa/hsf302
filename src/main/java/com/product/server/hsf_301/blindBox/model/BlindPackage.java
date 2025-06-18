package com.product.server.hsf_301.blindBox.model;


import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlindPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blind_id")
    private Integer id;
    
    @Column(name = "type_name", nullable = false, length = 50)
    private String name;

    private String imageUrl;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;
    private String imageType;

    @Column(name = "price_per_spin", nullable = false)
    private BigDecimal pricePerSpin;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active")
    private boolean isActive = true;
    
    @OneToMany(mappedBy = "blindBagType")
    private List<PrizeItem> prizeItems;
}


