package be.helha.maraichapp;

import be.helha.maraichapp.config.JwtService;
import be.helha.maraichapp.controllers.AuthController;
import be.helha.maraichapp.dto.AuthentificationDTO;
import be.helha.maraichapp.models.Jwt;
import be.helha.maraichapp.models.Rank;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.models.Validation;
import be.helha.maraichapp.repositories.JwtRepository;
import be.helha.maraichapp.repositories.UserRepository;
import be.helha.maraichapp.repositories.ValidationRepository;
import be.helha.maraichapp.services.UserService;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.Commit;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
public class AuthTest {

    public static final String EMAIL = "test@test.be";
    public static final String PASSWORD = "Password1";
    public static final String INVALIDEMAIL = "invalidemail";
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    ValidationRepository validationRepository;
    @Autowired
    JwtRepository jwtRepository;
    @Autowired
    List<Rank> ranks;
    String tokenBearer;

    Users users;

    @Test
    @Order(1)
    @Transactional
    @Commit
    public void testInscriptionInFirst(){
        userRepository.deleteAll();
        users = new Users("Castin", "Matteo", "0497306113", PASSWORD, "71", "Rue de Fosses", "5060", "Falisolle", EMAIL);
        userService.inscription(users);
        Users usersResult = userRepository.findByEmail(EMAIL).orElseThrow(() -> new RuntimeException("Not present in database"));
        assertEquals(users.getEmail(), usersResult.getEmail());
        assertEquals(users.getFirstName(), usersResult.getFirstName());
        assertEquals(users.getSurname(), usersResult.getSurname());
        assertEquals(users.getPhoneNumber(), usersResult.getPhoneNumber());
        assertEquals(users.getRank(), ranks.get(2));
    }

    @Test
    @Order(2)
    @Transactional
    @Commit
    public void testValidation(){
        Validation validation = validationRepository.findbyUser(EMAIL).orElseThrow(()-> new RuntimeException("No validation code for this user"));
        String code =  validation.getCode();
        Map<String, String> mapCode = new HashMap<>(1);
        mapCode.put("code", code);
        userService.activation(mapCode);
        Users users = userRepository.findByEmail(EMAIL).orElseThrow(() -> new RuntimeException("Not present in database"));
        assertTrue(users.isActif());
    }



    @Test
    @Order(3)
    @Transactional
    @Commit
    public void testConnection(){
        tokenBearer = jwtService.connection(new AuthentificationDTO(EMAIL, PASSWORD)).get("bearer");
        assertTrue(jwtRepository.existsByValue(tokenBearer));
    }

    @Test
    @Order(4)
    @Transactional
    @Commit
    public void testInvalidEmail() {
        Map<String, String> mapError;
        userRepository.deleteByEmail(INVALIDEMAIL);
        users = new Users("Castino", "Matteoo", "04973061143", PASSWORD, "712", "Rue de Fossess", "50600", "Falisollee", INVALIDEMAIL);
        mapError = userService.inscription(users);
        assertEquals(mapError.get("message"), "Your email is invalid !");
        mapError.clear();
        users.setEmail(EMAIL);
        mapError = userService.inscription(users);
        assertEquals(mapError.get("message"), "Your email is already used !");
    }

    @Test
    @Order(5)
    @Transactional
    @Commit
    public void testInvalidPassword() {
        Map<String, String> mapError;
        userRepository.deleteByEmail(EMAIL);
        users = new Users("Castino", "Matteoo", "04973061143", "password", "712", "Rue de Fossess", "50600", "Falisollee", EMAIL);
        mapError = userService.inscription(users);
        assertEquals(mapError.get("message"), "The password must contain minimum: 8 characters, 1 uppercase and 1 digit !");
    }

}
