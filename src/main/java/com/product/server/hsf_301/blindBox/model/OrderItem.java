package com.product.server.hsf_301.blindBox.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor

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

    public OrderItem(Order order, BlindPackage bp, PrizeItem prizeItem, Double price) {
        this.order = order;
        this.blindBagId = bp;
        this.prizeItemId = prizeItem;
        this.price = price;
    }
}
