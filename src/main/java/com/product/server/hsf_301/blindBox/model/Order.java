package com.product.server.hsf_301.blindBox.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.product.server.hsf_301.user.model.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "BlindBoxOrder")
@Table(name = "blind_box_orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;
    
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;
    
    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "payment_method")
    private String paymentMethod;

    private String shippingAddress;
    
    @Column(name = "payment_status")
    private String paymentStatus;
    
    @Column(name = "notes")
    private String notes;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    
    private List<OrderItem> orderItems;

    @PrePersist
    private void prePersist() {
        orderDate = LocalDateTime.now();
        paymentStatus = "PAID";
        paymentMethod = "PAYPAL";
        status = "PENDING";

    }
}
