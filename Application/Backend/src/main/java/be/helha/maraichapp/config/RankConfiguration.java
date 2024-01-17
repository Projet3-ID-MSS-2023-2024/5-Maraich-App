package be.helha.maraichapp.config;

import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RankConfiguration {
    @Bean
    public List<Rank> initializeRank(RankRepository rankRepository) {
        List<Rank> ranks = new ArrayList<>(3);

        // Check if the rank with RankEnum.CUSTOMER already exists
        if (rankRepository.findByName(RankEnum.CUSTOMER).isEmpty()) {
            ranks.add(new Rank(RankEnum.CUSTOMER, 1));
        }

        // Check if the rank with RankEnum.MARAICHER already exists
        if (rankRepository.findByName(RankEnum.MARAICHER).isEmpty()) {
            ranks.add(new Rank(RankEnum.MARAICHER, 2));
        }

        // Check if the rank with RankEnum.ADMINISTRATOR already exists
        if (rankRepository.findByName(RankEnum.ADMINISTRATOR).isEmpty()) {
            ranks.add(new Rank(RankEnum.ADMINISTRATOR, 3));
        }

        // Save only the ranks that do not exist in the database
        ranks.forEach(r -> {
            if (rankRepository.findByName(r.getName()).isEmpty()) {
                r.setIdRank(rankRepository.save(r).getIdRank());
            }
        });

        return ranks;
    }

}
