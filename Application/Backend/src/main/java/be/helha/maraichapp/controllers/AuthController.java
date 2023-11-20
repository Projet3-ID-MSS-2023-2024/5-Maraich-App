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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping(path = "inscription")
    public void inscription(@RequestBody Users users) {
        log.info("Inscription");
        this.userService.inscription(users);
    }

    @PostMapping(path = "activation")
    public void activation(@RequestBody Map<String, String> activation) {
        log.info("Activation");
        this.userService.activation(activation);
    }

    @PostMapping(path = "disconnection")
    public void disconnection() {
        log.info("Disconnection");
        this.jwtService.disconnection();
    }

    @PostMapping(path = "connection")
    public Map<String, String> connection(@RequestBody AuthentificationDTO authentificationDTO) {
        Authentication authentificate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authentificationDTO.email(), authentificationDTO.password())
            );

        if(authentificate.isAuthenticated()) {
            return this.jwtService.generate(authentificationDTO.email());
        }
        return null;
    }
}
