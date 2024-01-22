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
        users.add(new Users("admin",  "admin", "0464966942",passwordEncoder.encode("Admin123"), "33","Rue de la Tulipe", "4567","Jardinville", "admin@admin.be", ranks.get(0)));
        users.add(new Users("Dubois",  "Marie", "0498123456",passwordEncoder.encode("Client123"), "3","Rue de la Pomme", "1234","Jardinville", "client1@client.be", ranks.get(0)));
        users.add(new Users("Leroy",  "Jean", "0486123456",passwordEncoder.encode("Client123"), "12","Avenue des Poires", "7890","Fruitbourg", "client2@client.be", ranks.get(0)));
        users.add(new Users("Lambert",  "Sophie", "0464966942",passwordEncoder.encode("Maraicher123"), "7","Chemin de la Pomme", "2345","Verger-sur-Mer", "maraicher1@client.be", ranks.get(0)));
        users.add(new Users("Dupont",  "Pierre", "0479123456",passwordEncoder.encode("Maraicher123"), "45","Rue de la Courgette", "8901","Potagerville", "maraicher2@client.be", ranks.get(0)));
        users.add(new Users("Martin",  "Isabelle", "0467123456",passwordEncoder.encode("Maraicher123"), "18","Allée de l'Aubergine", "6789","Potager-les-Champs", "maraicher3@client.be", ranks.get(0)));

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
        users.get(0).setRank(ranks.get(1));
        userService.updateUserAdmin(users.get(3));
        userService.updateUserAdmin(users.get(4));
        userService.updateUserAdmin(users.get(5));
        userService.updateUserAdmin(users.get(0));
        users.get(0).setRank(ranks.get(2));
        userRepository.save(users.get(0));
        users.remove(5);
        users.remove(4);
        users.remove(3);
        users.remove(0);
        users.add(userRepository.findByEmail("maraicher1@client.be").get());
        users.add(userRepository.findByEmail("maraicher2@client.be").get());
        users.add(userRepository.findByEmail("maraicher3@client.be").get());
        users.add(userRepository.findByEmail("admin@admin.be").get());
        shops.add(new Shop(users.get(2).getShop().getIdShop(), "Fruité Sain", "maraicher1@maraicher.be", new Address("Rue de la Pomme", "1234", "Jardinville", "25"), "maraicher1.jpeg", "Fruité Sain offre une sélection fraîche de fruits et légumes de qualité. Notre mission est de promouvoir une alimentation saine et équilibrée pour toute la communauté.", true, true, users.get(2)));
        shops.add(new Shop(users.get(3).getShop().getIdShop(), "Légumes du Terroir", "maraicher2@maraicher.be", new Address("Avenue de la Courgette", "5678", "Potagerville", "8"), "maraicher2.jpeg", "Légumes du Terroir propose une variété de légumes cultivés localement, récoltés à maturité pour garantir la fraîcheur et le goût. Venez découvrir le meilleur du terroir dans notre magasin convivial.", true, true, users.get(3)));
        shops.add(new Shop(users.get(4).getShop().getIdShop(), "Exotif Fruits", "maraicher3@maraicher.be", new Address("Rue de l'Ananas", "9101", "Exotiqueville", "42"), "maraicher3.jpeg", "Exotif Fruits propose une expérience gustative unique avec une gamme exotique de fruits du monde entier. Notre équipe passionnée est là pour vous faire découvrir de nouvelles saveurs et vous aider à choisir les meilleurs fruits exotiques.", true, true, users.get(4)));
        shops.add(new Shop(users.get(5).getShop().getIdShop(), "Les Jardins du Soleil", "admin@maraicher.be", new Address("Chemin des Courges", "6789", "Potager-sur-Mer", "65"), "maraicher3.jpeg", "Les Jardins du Soleil sont fiers de cultiver une variété de légumes frais et de saison. Plongez dans l'expérience authentique de produits cultivés localement avec amour et engagement envers la durabilité. Venez vivre la fraîcheur du jardin à votre table.", true, false, users.get(5)));
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
