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
    @JoinColumn(name = "prize_item_id")
    private PrizeItem prizeItemId;

    private Double price;

    public OrderItem(Order order, PrizeItem prizeItem) {
        this.order = order;
        this.prizeItemId = prizeItem;
    }
}
