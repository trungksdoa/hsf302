package com.product.server.hsf_301.blindBox;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Customer_Spins")
@Data
public class CustomerSpin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spin_id")
    private Integer spinId;
    
    @Column(name = "customer_phone", nullable = false, length = 20)
    private String customerPhone;
    
    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;
    
    @ManyToOne
    @JoinColumn(name = "bag_type_id", nullable = false)
    private BlindBagType bagType;
    
    @Column(name = "spin_time")
    private LocalDateTime spinTime = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "won_item_id", nullable = false)
    private PrizeItem wonItem;
    
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    
    @Column(name = "status", length = 20)
    private String status = "pending";
    
    @OneToOne(mappedBy = "customerSpin", cascade = CascadeType.ALL)
    private SpinHistory spinHistory;
}