package com.product.server.hsf_301.blindBox.model;

import jakarta.persistence.*;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Order orderId;

    @ManyToOne
    private BlindPackage blindBagId;

    @ManyToOne
    private PrizeItem prizeItemId;

    private Double price;
}
