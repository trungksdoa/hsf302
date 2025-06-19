package com.product.server.hsf_301.blindBox.model;

import com.product.server.hsf_301.user.model.AppUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserPrizeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private SpinHistory spin;

    private boolean claimed = false;
    private LocalDateTime claimedAt;

    private boolean active = true; // Optional: nếu có chức năng huỷ
}