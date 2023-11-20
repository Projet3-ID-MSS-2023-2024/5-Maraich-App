package be.helha.maraichapp;

import be.helha.maraichapp.config.JwtService;
import be.helha.maraichapp.controllers.AuthController;
import be.helha.maraichapp.dto.AuthentificationDTO;
import be.helha.maraichapp.models.Jwt;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.models.Validation;
import be.helha.maraichapp.repositories.JwtRepository;
import be.helha.maraichapp.repositories.UserRepository;
import be.helha.maraichapp.repositories.ValidationRepository;
import be.helha.maraichapp.services.UserService;
import jakarta.transaction.Transactional;
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
@SpringBootTest
public class AuthTest {

    public static final String EMAIL = "matteo2010@live.be";
    public static final String PASSWORD = "Password1";
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ValidationRepository validationRepository;
    @Autowired
    JwtRepository jwtRepository;
    String tokenBearer;

    Users users;

    @Test
    @Order(1)
    @Transactional
    @Commit
    public void testInscription(){
        users = new Users("Castin", "Matteo", "0497306113", PASSWORD, "71", "Rue de Fosses", "5060", "Falisolle", EMAIL);
        userService.inscription(users);
        Users usersResult = userRepository.findByEmail(EMAIL).orElseThrow(() -> new RuntimeException("Not present in database"));
        assertEquals(users, usersResult);
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

//    @Test
//    @Order(4)
//    @Transactional
//    @Commit
//    public void testDisconnection(){
//       jwtService.disconnection();
//       Jwt jwt = jwtRepository.findByValue(tokenBearer).orElseThrow(()-> new RuntimeException("Token not found"));
//       assertTrue(jwt.isDisable());
//       assertTrue(jwt.isExpired());
//    }
}
