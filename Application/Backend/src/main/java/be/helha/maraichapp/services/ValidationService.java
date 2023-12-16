package be.helha.maraichapp.services;


import be.helha.maraichapp.models.*;
import be.helha.maraichapp.repositories.ValidationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
public class ValidationService {
    @Autowired
    private ValidationRepository validationRepository;
    @Autowired
    private EmailSender emailSender;

    public Validation createValidationProcess(Users users){
        Validation validation = new Validation();
        validation.setUsers(users);
        Instant creationDate = Instant.now();
        validation.setCreationDate(creationDate);
        Instant expirationDate = Instant.now();
        expirationDate = creationDate.plus(10, MINUTES);
        validation.setExpirationDate(expirationDate);
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(999999));
        validation.setCode(code);
        validation = this.validationRepository.save(validation);
        this.emailSender.sendActivationMail(validation);
        return validation;
    }

    public Validation readWithCode(String code) {
        return this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Your validation code is invalid !"));
    }
}
