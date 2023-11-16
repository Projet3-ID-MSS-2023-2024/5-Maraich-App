package be.helha.maraichapp.controllers;

import be.helha.maraichapp.services.EmailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailSender mailSender;

    @GetMapping("/send-email")
    public String sendEmail() {
        mailSender.sendEmail("logan-kawa@hotmail.com", "Test Subject", "Hello, this is a test email.");
        return "Email sent!";
    }
}
