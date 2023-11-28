package be.helha.maraichapp;

import be.helha.maraichapp.models.Rank;
import be.helha.maraichapp.models.RankEnum;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.RankRepository;
import be.helha.maraichapp.repositories.UserRepository;
import be.helha.maraichapp.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private RankRepository rankRepository;
    @Autowired

    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @AfterEach
    public void cleanup() {
        // Supprimez tous les utilisateurs après chaque test
        List<Users> allUsers = userService.getAllUsers();
        for (Users user : allUsers) {
            userService.deleteUserById(user.getIdUser());
        }
    }

    @Test
    @Order(1)
    @Transactional
    public void addUserTest1(){
        RankEnum rankEnum = RankEnum.CUSTOMER;

        Rank rank = new Rank(rankEnum, 1);
        Users testUser = new Users("Bilal",  "Maachi", "0456212365","HELHa123", "1","Rue des potiers", "6200","Châtelet", "bilal@test.be", rank, null);

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
        Users testUser = new Users("John", "Doe", "0123456789", "HELHa456", "2", "Avenue des Fleurs", "1000", "Bruxelles", "john@test.be", rank, null);


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
        Users testUser = new Users("Alice", "Smith", "0987654321", "HELHa789", "3", "Rue des Roses", "2000", "Anvers", "alice@test.be", rank, null);

        // Ajoutez l'utilisateur à la base de données
        Users savedUser = userService.addUser(testUser);

        // Mettez à jour les informations de l'utilisateur (e-mail, ID et rôle inchangés)
        savedUser.setFirstName("UpdatedFirstName");
        savedUser.setSurname("UpdatedSurname");
        savedUser.getAddress().setCity("UpdatedCity");
        savedUser.setEmail("updatedemail@test.be"); // Non autorisé

        // Créez une instance de RankEnum pour ADMINISTRATOR
        RankEnum adminRankEnum = RankEnum.ADMINISTRATOR;
        savedUser.getRank().setName(adminRankEnum); // Non autorisé

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
        assertEquals(rankEnum, retrievedUser.getRank().getName()); // Le rôle ne doit pas être modifié

    }



    @Test
    @Order(4)
    public void getUserByIdTest() {
        // Créez un utilisateur pour la recherche
        RankEnum rankEnum = RankEnum.CUSTOMER;
        Rank rank = new Rank(rankEnum, 1);
        Users testUser = new Users("Test", "User", "0123456789", "Test12356", "4", "Rue Test", "1234", "TestCity", "test@test.be", rank, null);

        // Ajoutez l'utilisateur à la base de données
        Users savedUser = userService.addUser(testUser);

        // Appelez la méthode getUserById
        Users retrievedUser = userService.getUserById(savedUser.getIdUser());

        // Vérifiez que l'utilisateur récupéré a les bonnes propriétés
        assertNotNull(retrievedUser);
        assertEquals(testUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(testUser.getSurname(), retrievedUser.getSurname());
        assertEquals(testUser.getEmail(), retrievedUser.getEmail());

    }

    @Test
    @Order(5)
    public void getUsersByRankTest() {
        // Créez un rang pour le test
        RankEnum rankEnum = RankEnum.CUSTOMER;
        Optional<Rank> rank = rankRepository.findByName(rankEnum);


        // Créez des utilisateurs pour le test
        Users testUser1 = new Users("User1", "Test", "123456789", "Test12345", "6", "Test Street", "12345", "TestCity", "test1@test.be", rank.get(), null);
        Users testUser2 = new Users("User2", "Test", "987654321", "Test45645", "7", "Test Avenue", "67890", "TestCity", "test2@test.be", rank.get(), null);

        // Ajoutez les utilisateurs à la base de données
        userService.addUser(testUser1);
        userService.addUser(testUser2);

        // Appelez la méthode getUsersByRank
        Optional<List<Users>> usersByRankOptional = userService.getUsersByRank(rankEnum);

        // Vérifiez que l'Optional n'est pas vide
        assertTrue(usersByRankOptional.isPresent());

        // Récupérez la liste d'utilisateurs de l'Optional
        List<Users> usersByRank = usersByRankOptional.get();

        // Vérifiez que la liste d'utilisateurs n'est pas vide
        assertFalse(usersByRank.isEmpty());

        // Vérifiez que chaque utilisateur dans la liste a le rang correct
        for (Users user : usersByRank) {
            assertEquals(rankEnum, user.getRank().getName());

        }
    }

    @Test
    @Order(6)
    public void testGetAllUsers() {
        // Ajoutez quelques utilisateurs pour le test
        Users testUser1 = new Users("User3", "Test", "123456789", "Test12345", "6", "Test Street", "12345", "TestCity", "test3@test.be", new Rank(RankEnum.CUSTOMER, 1), null);
        Users testUser2 = new Users("User4", "Test", "987654321", "Test45645", "7", "Test Avenue", "67890", "TestCity", "test4@test.be", new Rank(RankEnum.CUSTOMER, 1), null);

        // Ajoutez les utilisateurs à la base de données
        userService.addUser(testUser1);
        userService.addUser(testUser2);

        // Appelez la méthode getAllUsers
        List<Users> allUsers = userService.getAllUsers();

        // Vérifiez que la liste n'est pas vide
        assertFalse(allUsers.isEmpty());

        // Vérifiez que la liste contient au moins les utilisateurs ajoutés
        assertEquals(allUsers.get(0),testUser1);
        assertEquals(allUsers.get(1),testUser2);
    }

    @Test
    @Order(7)
    public void testDeleteUserById() {
        // Créez un utilisateur pour la suppression
        RankEnum rankEnum = RankEnum.CUSTOMER;
        Rank rank = new Rank(rankEnum, 1);
        Users testUser = new Users("UserToDelete", "Test", "123456789", "Test12345", "8", "Test Street", "12345", "TestCity", "testToDelete@test.be", rank, null);

        // Ajoutez l'utilisateur à la base de données
        Users savedUser = userService.addUser(testUser);

        // Appelez la méthode deleteUserById pour supprimer l'utilisateur ajouté
        userService.deleteUserById(savedUser.getIdUser());

        // Appelez la méthode getUserById pour vérifier si l'utilisateur a été supprimé
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(savedUser.getIdUser()));

        // Appelez la méthode getAllUsers pour vérifier la taille de la liste après la suppression
        List<Users> allUsersAfterDeletion = userService.getAllUsers();
        assertEquals(0, allUsersAfterDeletion.size());
    }

    @Test
    @Order(8)
    public void updateUserRestrictedWithPasswordChangeTest() {
        // Créez un utilisateur pour la mise à jour
        RankEnum rankEnum = RankEnum.CUSTOMER;
        Rank rank = new Rank(rankEnum, 1);
        Users testUser = new Users("Alice", "Smith", "0987654321", "HELHa789", "3", "Rue des Roses", "2000", "Anvers", "alice@test.be", rank, null);

        // Ajoutez l'utilisateur à la base de données
        Users savedUser = userService.addUser(testUser);


        savedUser.setFirstName("UpdatedFirstName");
        savedUser.setSurname("UpdatedSurname");
        savedUser.setPhoneNumber("9876543210");
        savedUser.setPassword("NewPassword123"); // Nouveau mot de passe;

        // Appelez la méthode updateUserRestricted
        Users resultUser = userService.updateUserRestricted(savedUser);

        // Vérifiez que l'utilisateur mis à jour a les bonnes propriétés
        assertNotNull(resultUser);
        assertEquals("UpdatedFirstName", resultUser.getFirstName());
        assertEquals("UpdatedSurname", resultUser.getSurname());
        assertEquals("9876543210", resultUser.getPhoneNumber());

        // Vérifiez que le mot de passe a été mis à jour
        assertNotEquals(savedUser.getPassword(), resultUser.getPassword());

        // Vérifiez que le mot de passe mis à jour est correct
        assertTrue(passwordEncoder.matches("NewPassword123", resultUser.getPassword()));
    }


}
