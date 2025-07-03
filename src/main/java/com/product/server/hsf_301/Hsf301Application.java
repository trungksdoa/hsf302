package com.product.server.hsf_301;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@SpringBootApplication
@EnableScheduling  // Enable scheduling support
public class Hsf301Application {

    public static void main(String[] args) {
        SpringApplication.run(Hsf301Application.class, args);

        BigDecimal testValeu = new BigDecimal(100);

        BigDecimal tes = new BigDecimal(400);

        System.out.println(testValeu.compareTo(tes));
    }
}
