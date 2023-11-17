package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    JavaMailSender javaMailSender;
    public void envoyer(Validation validation) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("no-reply@maraichapp.be");
        mailMessage.setTo(validation.getUsers().getEmail());
        mailMessage.setSubject("Your validation code");
        String text = String.format("Hello %s %S <br/> Your validation code is %s; See you soon",
        validation.getUsers().getFirstName(), validation.getUsers().getSurname(),
                validation.getCode());
        mailMessage.setText(text);
        javaMailSender.send(mailMessage);
    }
}
