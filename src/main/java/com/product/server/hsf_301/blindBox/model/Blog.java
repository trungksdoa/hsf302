package com.product.server.hsf_301.blindBox.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data

public class Blog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private  String title ;

    @ManyToOne
    private AppUser author ;
    private String content ;
    private String status ;
    private LocalDateTime createdAt ;

}
