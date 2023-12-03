package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Autowired
    private  JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    public void sendActivationMail(Validation validation) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        sendEmail(validation.getUsers().getEmail(), "Your validation code",
                String.format("Hello %s %s. Your validation code is %s; See you soon. Click here for activate your account -> http://localhost:4200/activation/%s",
                validation.getUsers().getFirstName(), validation.getUsers().getSurname(), validation.getCode(), validation.getCode()));
    }
}
