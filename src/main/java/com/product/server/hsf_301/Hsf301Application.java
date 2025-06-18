package com.product.server.hsf_301;

import com.product.server.hsf_301.blindBox.model.BlindPackage;
import com.product.server.hsf_301.blindBox.model.PrizeItem;
import com.product.server.hsf_301.blindBox.model.RareEnum;
import com.product.server.hsf_301.blindBox.repository.BlindBoxRepository;
import com.product.server.hsf_301.blindBox.repository.PrizeItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
public class Hsf301Application {

    public static void main(String[] args) {
        SpringApplication.run(Hsf301Application.class, args);
    }
}
