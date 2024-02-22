package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.repository.principal.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private UserRepository userRepository;

    // Méthode pour envoyer un mail à l'utilisateur
    @PostMapping(path = "/mail-confirm")
    public String sendEmail(@RequestBody String body) {
        // Convertit la chaîne JSON en objet JSON
        JSONObject json_body = new JSONObject(body);
        // Récupère l'e-mail à partir du JSON
        String email = json_body.getString("email");

        // Vérifier si l'e-mail existe dans la base de données
        if (!userRepository.existsByEmail(email)) {
            return "L'e-mail spécifié n'existe pas dans la base de données.";
        }

        String link = "http://localhost:8080/new-password?email="+email;
        String subject = "Réinitialisation de mot de passe";
        String text = "Bonjour,\n\n" +
                "Vous avez demandé à réinitialiser votre mot de passe.\n" +
                "Cliquez sur le lien ci-dessous pour créer un nouveau mot de passe :\n" +
                link + "\n\n" +
                "Si vous n'avez pas demandé cette réinitialisation, vous pouvez ignorer cet e-mail.\n\n" +
                "Cordialement,\n" +
                "Votre équipe EduCat";
        try {
            // Crée un objet SimpleMailMessage pour l'e-mail à envoyer
            SimpleMailMessage message = new SimpleMailMessage();
            // Définit le destinataire de l'e-mail
            message.setTo(email);
            message.setSubject(subject);
            message.setText(text);
            // Envoie l'e-mail
            emailSender.send(message);
            return "E-mail envoyé avec succès à " + email;
        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de l'envoi de l'e-mail à " + email;
        }
    }
}
