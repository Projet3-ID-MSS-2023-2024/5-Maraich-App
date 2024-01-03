package be.helha.maraichapp.config;

import be.helha.maraichapp.models.Rank;
import be.helha.maraichapp.models.RankEnum;
import be.helha.maraichapp.models.Shop;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.RankRepository;
import be.helha.maraichapp.repositories.ShopRepository;
import be.helha.maraichapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ShopConfiguration {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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


//    @Bean
//    public List<Users> initializeUsers(UserRepository usersRepository, List<Rank> ranks) {
//        List<Users> users = new ArrayList<>(5);
//        users.add(new Users("Matteo",  "Castin", "0464966942",passwordEncoder.encode("helha"), "3","Rue des potiers", "6200","Châtelet", "matteoCastin@leplusbg.be"));
//        users.add(new Users("Bilal", "Maachi", "0469696969",passwordEncoder.encode("helha"), "5","Rue des potiers", "6200", "Châtelet", "bilalMaachi@leplusbg.be"));
//        users.add(new Users("Loris", "Clement", "0442424242",passwordEncoder.encode("helha"), "1", "Rue des potiers", "6200", "Châtelet", "lorisClement@leplusbg.be"));
//        users.add(new Users("Esad", "Celik","0464966942", passwordEncoder.encode("helha"),"2", "Rue des potiers", "6200", "Châtelet", "esadCelik@leplusbg.be"));
//        users.add(new Users("Logan", "Dumont", "0477700467", passwordEncoder.encode("helha"),"4", "Rue des potiers", "6200", "Châtelet", "loganDumont@leplusbg.be"));
//        users.forEach(u -> u.setIdUser(
//                usersRepository.save(u)
//                        .getIdUser()));
//        return users;
//    }
//
//
//
//    @Bean
//    public List<Shop> initializeShop(ShopRepository shopRepository, List<Users> users) {
//        List<Shop> shops = new ArrayList<>(5);
//        shops.add(new Shop("Chez Robert", "robert@gmail.com", "Rue de potier", "3", "6200", "Châtelet", "src/assets/images/shop/Patrick.jpg", "des bon légumes bien frais", users.get(0)));
//        shops.add(new Shop("Chez Norbert", "norbert@gmail.com", "Rue de potier", "5","6200", "Châtelet", "src/assets/images/shop/Patrick.jpg", "des bon légumes bien frais", users.get(1)));
//        shops.add(new Shop("Chez Patrick", "patrick@gmail.com", "Rue de potier","1", "6200", "Châtelet", "src/assets/images/shop/Patrick.jpg", "des bon légumes bien frais", users.get(2)));
//        shops.add(new Shop("Chez Franck", "franck@gmail.com", "Rue de potier", "2","6200", "Châtelet", "src/assets/images/shop/Patrick.jpg", "des bon légumes bien frais", users.get(3)));
//        shops.add(new Shop("Chez Roger", "roger@gmail.com", "Rue de potier", "4", "6200", "Châtelet", "src/assets/images/shop/Patrick.jpg", "des bon légumes bien frais", users.get(4)));
//        shops.forEach(s -> s.setIdShop((shopRepository.save(s).getIdShop())));
//        return shops;
//    }

}
