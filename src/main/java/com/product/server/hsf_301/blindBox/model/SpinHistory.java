package com.product.server.hsf_301.blindBox.model;

import com.product.server.hsf_301.user.model.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpinHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private PackagesBox blindBagId;

    @ManyToOne
    private PrizeItem prizeItemId; // Có thể null nếu quay lỗi

    private Double price;

    private Boolean success;

    private LocalDateTime spinTime;

    // Add getter method for consistency
    public Integer getId() {
        return this.historyId;
    }

    // Fix Boolean to boolean for consistent naming
    public Boolean getSuccess() {
        return this.success;
    }
}