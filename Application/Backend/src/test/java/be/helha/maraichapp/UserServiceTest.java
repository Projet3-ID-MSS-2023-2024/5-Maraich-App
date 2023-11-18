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
        assertEquals(testUser.getRank().getName(), rank.getName());

    }

    @Test
    @Order(2)
    public void updateUserTest() {
        // Créez un utilisateur pour la mise à jour
        RankEnum rankEnum = RankEnum.CUSTOMER;
        Rank rank = new Rank(rankEnum, 1);
        Users testUser = new Users("John", "Doe", "0123456789", "HELHa456", "2", "Avenue des Fleurs", "1000", "Bruxelles", "john@test.be", rank);


        // Ajoutez l'utilisateur à la base de données
        Users savedUser = userService.addUser(testUser);

        // Mettez à jour les informations de l'utilisateur
        savedUser.setFirstName("UpdatedFirstName");
        savedUser.setSurname("UpdatedSurname");
        savedUser.getAddress().setCity("UpdatedCity");

        // Appelez la méthode updateUser
        Users updatedUser = userService.updateUserAdmin(savedUser);

        // Récupérez l'utilisateur mis à jour de la base de données
        Users retrievedUser = userRepository.findById(updatedUser.getIdUser()).orElse(null);

        // vérifier que l'utilisateur mis à jour a les bonnes propriétés
        assertNotNull(retrievedUser);
        assertEquals("UpdatedFirstName", retrievedUser.getFirstName());
        assertEquals("UpdatedSurname", retrievedUser.getSurname());
        assertEquals("UpdatedCity", retrievedUser.getAddress().getCity());
    }

    @Test
    @Order(3)
    public void updateUserRestricted() {
        // Créez un utilisateur pour la mise à jour
        RankEnum rankEnum = RankEnum.CUSTOMER;
        Rank rank = new Rank(rankEnum, 1);
        Users testUser = new Users("Alice", "Smith", "0987654321", "HELHa789", "3", "Rue des Roses", "2000", "Anvers", "alice@test.be", rank);

        // Ajoutez l'utilisateur à la base de données
        Users savedUser = userService.addUser(testUser);

        // Mettez à jour les informations de l'utilisateur (e-mail, ID et rôle inchangés)
        savedUser.setFirstName("UpdatedFirstName");
        savedUser.setSurname("UpdatedSurname");
        savedUser.getAddress().setCity("UpdatedCity");
        savedUser.setEmail("updatedemail@test.be"); // Non autorisé
        savedUser.getRank().setName(RankEnum.valueOf("ADMINISTRATOR")); // Non autorisé

        // Appelez la méthode updateUserRestricted
        Users updatedUser = userService.updateUserRestricted(savedUser);

        // Récupérez l'utilisateur mis à jour de la base de données
        Users retrievedUser = userRepository.findById(updatedUser.getIdUser()).orElse(null);

        // Vérifiez que l'utilisateur mis à jour a les bonnes propriétés (e-mail, ID et rôle inchangés)
        assertNotNull(retrievedUser);
        assertEquals("UpdatedFirstName", retrievedUser.getFirstName());
        assertEquals("UpdatedSurname", retrievedUser.getSurname());
        assertEquals("UpdatedCity", retrievedUser.getAddress().getCity());
        assertEquals("alice@test.be", retrievedUser.getEmail());
        assertEquals(1, retrievedUser.getIdUser()); // L'ID ne doit pas être modifié
        assertEquals("CUSTOMER", retrievedUser.getRank().getName()); // Le rôle ne doit pas être modifié
    }

}
