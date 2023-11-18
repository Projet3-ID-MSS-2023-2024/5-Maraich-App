package be.helha.maraichapp;

import be.helha.maraichapp.models.Rank;
import be.helha.maraichapp.models.RankEnum;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.RankRepository;
import be.helha.maraichapp.repositories.UserRepository;
import be.helha.maraichapp.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private RankRepository rankRepository;
    @Autowired

    private UserService userService;
    @Autowired

    private UserRepository userRepository;

    @Test
    @Order(1)
    @Transactional
    public void addUserTest1(){
        RankEnum rankEnum = RankEnum.CUSTOMER;

        Rank rank = new Rank(rankEnum, 1);
        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be", rank);

        Users savedUser = userService.addUser(testUser);

        // Récupérez l'utilisateur de la base de données
        Users retrievedUser = userRepository.findById(savedUser.getIdUser()).orElse(null);

        // Vérifiez que l'utilisateur n'est pas nul (présent dans la base de données)
        assertNotNull(retrievedUser);

        // Assurez-vous que les propriétés de l'utilisateur sont correctes
        assertEquals(testUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(testUser.getSurname(), retrievedUser.getSurname());

    }
}
