package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import java.util.Base64;

@RestController
public class PasswordController {

    @Autowired
    private Cipher aesCipher;

    public String encrypt(String data) throws Exception {
        byte[] encryptedBytes = aesCipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @Autowired
    private UserRepository userRepository;

    // Méthode pour réinitialiser le mot de passe de l'utilisateur
    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody String body) throws Exception {
        JSONObject jsonBody = new JSONObject(body);

        // Récupération de l'e-mail et du nouveau mot de passe à partir du corps JSON
        String email = jsonBody.getString("email");
        String newPassword = encrypt(jsonBody.getString("newPassword"));

        // Recherche de l'utilisateur dans la base de données par e-mail
        User user = userRepository.findByEmail(email);
        // Vérification si l'utilisateur existe
        if (user == null) {
            return "{\"error\": \"Utilisateur non trouvé.\"}";
        }

        // Mettre à jour le mot de passe de l'utilisateur dans la base de données en utilisant setUserPassword
        userRepository.setUserPassword(user.getId(), newPassword);

        return "{\"message\": \"Mot de passe réinitialisé avec succès.\"}";
    }
}


