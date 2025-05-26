package com.product.server.hsf_301.blindBox.model;

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
    private User user;

    @ManyToOne
    private BlindBagType blindBagId;

    @ManyToOne
    private PrizeItem prizeItemId; // Có thể null nếu quay lỗi

    private Double price;

    private Boolean success;

    private boolean redeemed;
    private LocalDateTime redeemedAt;

    private LocalDateTime spinTime;

    private String errorMessage; // Có thể null nếu thành công
}