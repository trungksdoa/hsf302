package com.product.server.hsf_301.payment.model;


import jakarta.persistence.*;

@Entity
public class TopUpHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String transaction_id;

    private String amount;

    private boolean status;

    private String created_at;

    private String updated_at;


    @PrePersist
    public void prePersist() {
        created_at = java.time.LocalDateTime.now().toString();
        updated_at = java.time.LocalDateTime.now().toString();
    }

    @PreUpdate
    public void preUpdate() {
        updated_at = java.time.LocalDateTime.now().toString();
    }


}
