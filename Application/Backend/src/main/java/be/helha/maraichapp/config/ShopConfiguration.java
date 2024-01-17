package be.helha.maraichapp.config;

import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.*;
import be.helha.maraichapp.services.ShopService;
import be.helha.maraichapp.services.UserService;
import be.helha.maraichapp.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Profile("!test")
public class ShopConfiguration {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ValidationService validationService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopService shopService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;

    @Bean
    public List<Users> initializeUsers(List<Rank> ranks) {
        List<Users> users = new ArrayList<>(3);
        Map<String, String> mapString = new HashMap<>();
        users.add(new Users("admin",  "admin", "0464966942",passwordEncoder.encode("Admin123"), "3","Rue des potiers", "6200","Châtelet", "admin@admin.be", ranks.get(2)));
        users.add(new Users("clientun",  "clientun", "0464966942",passwordEncoder.encode("Client123"), "3","Rue des potiers", "6200","Châtelet", "client1@client.be", ranks.get(0)));
        users.add(new Users("clientdeux",  "clientdeux", "0464966942",passwordEncoder.encode("Client123"), "3","Rue des potiers", "6200","Châtelet", "client2@client.be", ranks.get(0)));
        users.add(new Users("maraicherdeux",  "maraicherdeux", "0464966942",passwordEncoder.encode("Maraicher123"), "3","Rue des potiers", "6200","Châtelet", "maraicher1@client.be", ranks.get(0)));
        users.add(new Users("maraicherdeux",  "maraicherdeux", "0464966942",passwordEncoder.encode("Maraicher123"), "3","Rue des potiers", "6200","Châtelet", "maraicher2@client.be", ranks.get(0)));
        users.add(new Users("maraichertrois",  "maraichertrois", "0464966942",passwordEncoder.encode("Maraicher123"), "3","Rue des potiers", "6200","Châtelet", "maraicher3@client.be", ranks.get(0)));

        users.forEach(u -> u.setIdUser(
                userRepository.save(u)
                        .getIdUser()));
        users.forEach(u -> {
            Validation validation = validationService.createValidationProcess(u);
            mapString.put("code", validation.getCode());
            userService.activation(mapString);
            u.setValidation(validation);
            u.setActif(true);
            userRepository.save(u);
        });

        return users;
    }

    @Bean
    public List<Shop> initializeShops(List<Users> users, List<Rank> ranks){
        List<Shop> shops = new ArrayList<>();
        users.get(3).setRank(ranks.get(1));
        users.get(4).setRank(ranks.get(1));
        users.get(5).setRank(ranks.get(1));
        userService.updateUserAdmin(users.get(3));
        userService.updateUserAdmin(users.get(4));
        userService.updateUserAdmin(users.get(5));
        users.remove(5);
        users.remove(4);
        users.remove(3);
        users.add(userRepository.findByEmail("maraicher1@client.be").get());
        users.add(userRepository.findByEmail("maraicher2@client.be").get());
        users.add(userRepository.findByEmail("maraicher3@client.be").get());
        shops.add(new Shop(users.get(3).getShop().getIdShop(), "Marché n°1", "maraicher1@maraicher.be", new Address("Rue des potiers", "6200", "Châtelet", "3"), "maraicher1.jpeg", "Marché numéro 1", true, true, users.get(3)));
        shops.add(new Shop(users.get(4).getShop().getIdShop(), "Marché n°2", "maraicher2@maraicher.be", new Address("Rue des potiers", "6200", "Châtelet", "3"), "maraicher2.jpeg", "Marché numéro 2", true, true, users.get(4)));
        shops.add(new Shop(users.get(5).getShop().getIdShop(), "Marché n°3", "maraicher3@maraicher.be", new Address("Rue des potiers", "6200", "Châtelet", "3"), "maraicher3.jpeg", "Marché numéro 2", true, true, users.get(5)));
        shops.forEach(s -> shopService.updateShop(s));
        return shops;
    }

    @Bean
    public List<Category> initializeCategories(){
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Légumes"));
        categories.add(new Category("Fruits"));
        categories.add(new Category("Féculents"));
        categories.forEach(c -> c.setIdCategory(
                categoryRepository.save(c)
                        .getIdCategory()
        ));
        return categories;
    }

    @Bean
    public List<Product> initializeProducts(List<Category> categories, List<Shop> shops){
        List<Product> products = new ArrayList<>();
        products.add(new Product("Carotte", 10.0, "Carotte bien orange", "Carotte.jpg", 0, 12.5, false, categories.get(0), shops.get(0)));
        products.add(new Product("Pomme de terre", 3.0, "Pomme frite", "Patate.jpeg", 0, 133.0, false, categories.get(2), shops.get(0)));
        products.add(new Product("Laitue", 2.75, "Laitue 0 limace", "Laitue.jpg", 125, 0.0, true, categories.get(0), shops.get(1)));
        products.add(new Product("Tomate", 15, "Des tomates rouge et mur", "Tomate.jpg", 0, 120.0, false, categories.get(1), shops.get(1)));
        products.add(new Product("Betterave", 2.75, "Betterave justeuse", "Betterave.jpg", 0, 127.74, false, categories.get(0), shops.get(2)));
        products.add(new Product("Orange", 15, "Des tomates rouge et mur", "Orange.jpg", 75, 0.0, true, categories.get(1), shops.get(2)));
        products.forEach(p -> p.setId(
                productRepository.save(p)
                        .getId()
        ));
        return products;
    }


}
