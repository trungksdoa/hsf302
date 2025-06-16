package com.product.server.hsf_301.blindBox.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data

public class Blog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private  String title ;
    private String author ;
    private String content ;
    private String status ;
    private LocalDateTime createdAt ;

}
