package com.product.server.hsf_301.payment.model;


import com.product.server.hsf_301.user.model.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class TopUpHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private AppUser user;

    private String transaction_id;

    private String amount;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at") 
    private LocalDateTime updatedAt;

    // Compatibility getters/setters for existing code
    public String getCreated_at() {
        return createdAt != null ? createdAt.toString() : null;
    }

    public void setCreated_at(String created_at) {
        this.createdAt = LocalDateTime.now();
    }

    public String getUpdated_at() {
        return updatedAt != null ? updatedAt.toString() : null;
    }

    public void setUpdated_at(String updated_at) {
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
