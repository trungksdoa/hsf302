package com.product.server.hsf_301.blindBox.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "blind_bag_id")
    private BlindPackage blindBagId;

    @ManyToOne
    @JoinColumn(name = "prize_item_id")
    private PrizeItem prizeItemId;

    private Double price;
}
