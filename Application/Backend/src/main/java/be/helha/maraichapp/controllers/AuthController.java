package be.helha.maraichapp.controllers;

import be.helha.maraichapp.dto.AuthentificationDTO;
import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.repositories.UserRepository;
import be.helha.maraichapp.config.JwtService;
import be.helha.maraichapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping(path = "signup")
    public Map<String, String> inscription(@RequestBody Users users) {
        log.info("Inscription");
        return this.userService.inscription(users);
    }

    @PostMapping(path = "activation")
    public Map<String, String> activation(@RequestBody Map<String, String> activation) {
        log.info("Activation");
        return this.userService.activation(activation);
    }

    @PostMapping(path = "login")
    public Map<String, String> connection(@RequestBody AuthentificationDTO authentificationDTO) {
        return this.jwtService.connection(authentificationDTO);
    }

}
