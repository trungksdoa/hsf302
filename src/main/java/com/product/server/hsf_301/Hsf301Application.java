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


    @Bean
    public CommandLineRunner initializeData(BlindBoxRepository blindBagTypeRepository, PrizeItemRepository prizeItemRepository) {
        return args -> {

            if (blindBagTypeRepository.count() != 0) return;
            // Anime Series
            BlindPackage animeSeries = new BlindPackage();
            animeSeries.setName("Anime Series");
            animeSeries.setPricePerSpin(new BigDecimal("9.99"));
            animeSeries.setDescription("Collection of anime character blind boxes");
            animeSeries.setActive(true);

            // Disney Series
            BlindPackage disneySeries = new BlindPackage();
            disneySeries.setName("Disney Series");
            disneySeries.setPricePerSpin(new BigDecimal("12.99"));
            disneySeries.setDescription("Collection of Disney character blind boxes");
            disneySeries.setActive(true);

            // Gaming Series
            BlindPackage gamingSeries = new BlindPackage();
            gamingSeries.setName("Gaming Series");
            gamingSeries.setPricePerSpin(new BigDecimal("14.99"));
            gamingSeries.setDescription("Collection of gaming character blind boxes");
            gamingSeries.setActive(true);

            // Superhero Series
            BlindPackage superheroSeries = new BlindPackage();
            superheroSeries.setName("Superhero Series");
            superheroSeries.setPricePerSpin(new BigDecimal("13.99"));
            superheroSeries.setDescription("Collection of superhero character blind boxes");
            superheroSeries.setActive(true);

            BlindPackage basicBox = new BlindPackage();
            basicBox.setId(1);
            basicBox.setName("Basic Box");
            basicBox.setPricePerSpin(new BigDecimal("10.00"));
            basicBox.setDescription("This is a basic box.");
            basicBox.setActive(true);

            // Add more initial data as needed

            // Save all blind bag types
            BlindPackage anime = blindBagTypeRepository.save(animeSeries);
            BlindPackage disney = blindBagTypeRepository.save(disneySeries);
            BlindPackage gaming = blindBagTypeRepository.save(gamingSeries);
            BlindPackage hero = blindBagTypeRepository.save(superheroSeries);
            BlindPackage basic = blindBagTypeRepository.save(basicBox);

            //add prize item

            // Initialize prize items for Anime Series
            prizeItemRepository.saveAll(List.of(
                    PrizeItem.builder().itemName("Naruto Figure").itemImage("/images/anime/naruto.jpg")
                            .rarity(RareEnum.RARE).blindBagType(anime).probability(0.05).isActive(true).build(),
                    PrizeItem.builder().itemName("One Piece Keychain").itemImage("/images/anime/onepiece.jpg")
                            .rarity(RareEnum.UNCOMMON).blindBagType(anime).probability(0.15).isActive(true).build(),
                    PrizeItem.builder().itemName("Dragon Ball Sticker").itemImage("/images/anime/dragonball.jpg")
                            .rarity(RareEnum.COMMON).blindBagType(anime).probability(0.30).isActive(true).build(),
                    PrizeItem.builder().itemName("Limited Edition Goku").itemImage("/images/anime/goku_limited.jpg")
                            .rarity(RareEnum.SPECIAL).blindBagType(anime).probability(0.02).isActive(true).build(),
                    PrizeItem.builder().itemName("Better luck next time!").itemImage("/images/common/luck.jpg")
                            .rarity(RareEnum.GOOD_LUCK).blindBagType(anime).probability(0.48).isActive(true).build()
            ));

            // Initialize prize items for Disney Series
            prizeItemRepository.saveAll(List.of(
                    PrizeItem.builder().itemName("Mickey Mouse Plush").itemImage("/images/disney/mickey.jpg")
                            .rarity(RareEnum.UNCOMMON).blindBagType(disney).probability(0.15).isActive(true).build(),
                    PrizeItem.builder().itemName("Frozen Elsa Figurine").itemImage("/images/disney/elsa.jpg")
                            .rarity(RareEnum.RARE).blindBagType(disney).probability(0.05).isActive(true).build(),
                    PrizeItem.builder().itemName("Lion King Pin").itemImage("/images/disney/lionking.jpg")
                            .rarity(RareEnum.COMMON).blindBagType(disney).probability(0.25).isActive(true).build(),
                    PrizeItem.builder().itemName("Walt Disney Signature Card").itemImage("/images/disney/signature.jpg")
                            .rarity(RareEnum.SPECIAL).blindBagType(disney).probability(0.01).isActive(true).build(),
                    PrizeItem.builder().itemName("Try again next time!").itemImage("/images/common/luck.jpg")
                            .rarity(RareEnum.GOOD_LUCK).blindBagType(disney).probability(0.54).isActive(true).build()
            ));

            // Initialize prize items for Gaming Series
            prizeItemRepository.saveAll(List.of(
                    PrizeItem.builder().itemName("Mario Figurine").itemImage("/images/gaming/mario.jpg")
                            .rarity(RareEnum.UNCOMMON).blindBagType(gaming).probability(0.12).isActive(true).build(),
                    PrizeItem.builder().itemName("Master Sword Keychain").itemImage("/images/gaming/zelda.jpg")
                            .rarity(RareEnum.RARE).blindBagType(gaming).probability(0.08).isActive(true).build(),
                    PrizeItem.builder().itemName("Minecraft Sticker Pack").itemImage("/images/gaming/minecraft.jpg")
                            .rarity(RareEnum.COMMON).blindBagType(gaming).probability(0.30).isActive(true).build(),
                    PrizeItem.builder().itemName("Collector's Edition Controller").itemImage("/images/gaming/controller.jpg")
                            .rarity(RareEnum.SPECIAL).blindBagType(gaming).probability(0.02).isActive(true).build(),
                    PrizeItem.builder().itemName("Game Over! Try again.").itemImage("/images/common/gameover.jpg")
                            .rarity(RareEnum.GOOD_LUCK).blindBagType(gaming).probability(0.48).isActive(true).build()
            ));

            // Initialize prize items for Superhero Series
            prizeItemRepository.saveAll(List.of(
                    PrizeItem.builder().itemName("Spider-Man Action Figure").itemImage("/images/superhero/spiderman.jpg")
                            .rarity(RareEnum.UNCOMMON).blindBagType(hero).probability(0.15).isActive(true).build(),
                    PrizeItem.builder().itemName("Batman Utility Belt").itemImage("/images/superhero/batman.jpg")
                            .rarity(RareEnum.RARE).blindBagType(hero).probability(0.07).isActive(true).build(),
                    PrizeItem.builder().itemName("Avengers Logo Pin").itemImage("/images/superhero/avengers.jpg")
                            .rarity(RareEnum.COMMON).blindBagType(hero).probability(0.28).isActive(true).build(),
                    PrizeItem.builder().itemName("Signed Stan Lee Card").itemImage("/images/superhero/stanlee.jpg")
                            .rarity(RareEnum.SPECIAL).blindBagType(hero).probability(0.01).isActive(true).build(),
                    PrizeItem.builder().itemName("Not a hero today. Try again!").itemImage("/images/common/luck.jpg")
                            .rarity(RareEnum.GOOD_LUCK).blindBagType(hero).probability(0.49).isActive(true).build()
            ));

            // Initialize prize items for Basic Box
            prizeItemRepository.saveAll(List.of(
                    PrizeItem.builder().itemName("Basic Toy").itemImage("/images/basic/toy.jpg")
                            .rarity(RareEnum.COMMON).blindBagType(basic).probability(0.40).isActive(true).build(),
                    PrizeItem.builder().itemName("Collectible Card").itemImage("/images/basic/card.jpg")
                            .rarity(RareEnum.UNCOMMON).blindBagType(basic).probability(0.20).isActive(true).build(),
                    PrizeItem.builder().itemName("Mystery Keychain").itemImage("/images/basic/keychain.jpg")
                            .rarity(RareEnum.RARE).blindBagType(basic).probability(0.05).isActive(true).build(),
                    PrizeItem.builder().itemName("Better luck next time!").itemImage("/images/common/luck.jpg")
                            .rarity(RareEnum.GOOD_LUCK).blindBagType(basic).probability(0.35).isActive(true).build()
            ));
        };
    }
}
