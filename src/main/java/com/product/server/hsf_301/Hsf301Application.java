package com.product.server.hsf_301;

import com.product.server.hsf_301.blindBox.model.BlindBagType;
import com.product.server.hsf_301.blindBox.repository.BlindBoxRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootApplication
public class Hsf301Application {

    public static void main(String[] args) {
        SpringApplication.run(Hsf301Application.class, args);
    }


    @Bean
    public CommandLineRunner initializeData(BlindBoxRepository blindBagTypeRepository) {
        return args -> {
            // Anime Series
            BlindBagType animeSeries = new BlindBagType();
            animeSeries.setTypeName("Anime Series");
            animeSeries.setPricePerSpin(new BigDecimal("9.99"));
            animeSeries.setDescription("Collection of anime character blind boxes");
            animeSeries.setActive(true);

            // Disney Series
            BlindBagType disneySeries = new BlindBagType();
            disneySeries.setTypeName("Disney Series");
            disneySeries.setPricePerSpin(new BigDecimal("12.99"));
            disneySeries.setDescription("Collection of Disney character blind boxes");
            disneySeries.setActive(true);

            // Gaming Series
            BlindBagType gamingSeries = new BlindBagType();
            gamingSeries.setTypeName("Gaming Series");
            gamingSeries.setPricePerSpin(new BigDecimal("14.99"));
            gamingSeries.setDescription("Collection of gaming character blind boxes");
            gamingSeries.setActive(true);

            // Superhero Series
            BlindBagType superheroSeries = new BlindBagType();
            superheroSeries.setTypeName("Superhero Series");
            superheroSeries.setPricePerSpin(new BigDecimal("13.99"));
            superheroSeries.setDescription("Collection of superhero character blind boxes");
            superheroSeries.setActive(true);

            BlindBagType basicBox = new BlindBagType();
            basicBox.setBagTypeId(1);
            basicBox.setTypeName("Basic Box");
            basicBox.setPricePerSpin(new BigDecimal("10.00"));
            basicBox.setDescription("This is a basic box.");
            basicBox.setActive(true);

            // Add more initial data as needed

            // Save all blind bag types
            blindBagTypeRepository.saveAll(Arrays.asList(
                    animeSeries, disneySeries, gamingSeries, superheroSeries, basicBox
            ));
        };
    }
}
