package com.product.server.hsf_301.blindBox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.product.server.hsf_301.user.model.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPityStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private PackagesBox packagesBox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private AppUser appUser;

    private int current_pity;

    private int count;
}
