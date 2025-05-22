package com.product.server.hsf_301.blindBox;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Spin_History")
@Data
public class SpinHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;
    
    @OneToOne
    @JoinColumn(name = "spin_id", nullable = false)
    private CustomerSpin customerSpin;
    
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    @Column(name = "device_info")
    private String deviceInfo;
}