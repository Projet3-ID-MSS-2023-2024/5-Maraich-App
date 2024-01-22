package be.helha.maraichapp.services;

import be.helha.maraichapp.models.Users;
import be.helha.maraichapp.models.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {

    @Autowired
    private  JavaMailSender javaMailSender;
    SimpleMailMessage message = new SimpleMailMessage();

    public void sendEmail(String to, String subject, String body) {
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }

    public void sendActivationMail(Validation validation) {
        String subject = "Activation de votre compte";
        String body = String.format("Bonjour %s %s,\n\n"
                        + "Nous espérons que ce message vous trouve bien. Pour finaliser l'activation de votre compte, veuillez utiliser le code suivant : %s.\n\n"
                        + "Cliquez ici pour activer votre compte : https://localhost:4200/activation/%s\n\n"
                        + "Nous sommes impatients de vous accueillir au sein de notre communauté. Si vous avez la moindre question ou rencontrez des difficultés, n'hésitez pas à nous contacter.\n\n"
                        + "À bientôt !\n\n"
                        + "Cordialement,\nMaraich'App\n5MaraichApp@gmail.com",
                validation.getUsers().getFirstName(), validation.getUsers().getSurname(), validation.getCode(), validation.getCode());

        sendEmail(validation.getUsers().getEmail(), subject, body);
    }

    public void sendInscriptionIsConfirm(Users users) {
        String subject = "Bienvenue chez Maraich'App";
        String message = String.format("Bonjour %s %s,\n\n"
                        + "Nous sommes ravis de vous accueillir chez Maraich'App. Votre compte a été activé avec succès.\n\n"
                        + "Nous sommes à votre disposition pour toute assistance ou question. N'hésitez pas à nous contacter si besoin.\n\n"
                        + "Nous espérons que votre expérience avec Maraich'App sera des plus agréables.\n\n"
                        + "Cordialement,\nL'équipe Maraich'App",
                users.getFirstName(), users.getSurname());

        sendEmail(users.getEmail(), subject, message);
    }

    // Envoi d'un mail au maraicher lorsqu'une commande a été effectuée sur son shop
    public void sendNewOrderEmail(Users users){
        String subject = "Nouvelle commande";
        String message = String.format( "Bonjour %s %s, \n\n"
                        + "Nous sommes ravis de vous informer qu'une nouvelle commande a été passée sur votre magasin en ligne.\n\n"
                        + "Pour voir les détails complets et gérer la commande, veuillez vous connecter à votre compte et accéder à l'onglet \"Gérer mes commandes\" depuis votre tableau de bord."
                        + "Merci de votre attention et bonne préparation !\n\n"
                        + "Cordialement,\nL'équipe Maraich'App",
                users.getFirstName(), users.getSurname()
        );
        sendEmail(users.getEmail(), subject, message);
    }

    public void sendOrderReadyEmail(Users users){
        String subject = "Nouvelle commande";
        String message = String.format( "Cher(e) %s %s, \n\n"
                        +"Nous sommes heureux de vous informer que votre commande est maintenant prête pour récupération."
                        + "Cordialement,\nL'équipe Maraich'App",
                users.getFirstName(), users.getSurname()
        );
        sendEmail(users.getEmail(), subject, message);
    }
}
