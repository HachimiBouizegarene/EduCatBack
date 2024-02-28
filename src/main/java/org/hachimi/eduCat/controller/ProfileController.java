package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.NotValidJwsException;
import org.hachimi.eduCat.Exceptions.FailUpdatePasswordException;
import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotValidPasswordException;
import org.hachimi.eduCat.entity.principal.Product;
import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.repository.principal.PossessesRepository;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.hachimi.eduCat.service.JWTService;
import org.hachimi.eduCat.service.UserService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@RestController
public class ProfileController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PossessesRepository possessesRepository;
    @PostMapping(path = "/getProfile")
    public String getProfile(@RequestBody String body_str) {
        JSONObject body = new JSONObject(body_str);
        JSONObject ret = new JSONObject();
        try{
            String jws;
            try{
                jws = body.getString("jws");
                JWTService.verifyJWT(jws);
            }catch (JSONException e){
                throw new InformationsException();
            }

            JSONObject payload = JWTService.getPayload(jws);
            Optional<User> userOptional = userRepository.findById(payload.getInt("id"));

            if (userOptional.isPresent()){
                User user  = userOptional.get();
                ret = user.GetInfos(false);

            }else{
                // Je doute...
                throw new NotValidJwsException();
            }

        }catch (Exception e){
            ret.put("error", e.getMessage());
            e.printStackTrace();
        }
        return ret.toString();
    }
    @PostMapping(path = "/updateProfile")
    public String updateProfile(@RequestBody String body_str) {
        JSONObject ret = new JSONObject();
        JSONObject body = new JSONObject(body_str);
        try{
            String jws;
            try{
                jws = body.getString("jws");
                JWTService.verifyJWT(jws);
                body.remove("jws");
            }catch (JSONException e){
                throw new InformationsException();
            }

            JSONObject payload = JWTService.getPayload(jws);
            // TODO : Gerer les cas de duplications (pseudo, email)
            // TODO : Gerer les cas de Foreign Key
            userRepository.setUserInformations(payload.getInt("id"), body);
            ret.put("success","Mis a jour avec succes !");
        }catch (Exception e){
            ret.put("error", e.getMessage());
            e.printStackTrace();
        }

        return ret.toString();
    }

    @PostMapping(path = "/updatePassword")
    public String updatePasseword(@RequestBody String body_str) {
        JSONObject ret = new JSONObject();
        JSONObject body = new JSONObject(body_str);
        try{
            String jws;
            String oldPassword;
            String newPassword;
            try{
                jws = body.getString("jws");
                JWTService.verifyJWT(jws);
                body.remove("jws");
                oldPassword = body.getString("oldPassword");
                newPassword = body.getString("newPassword");
            }catch (JSONException e){
                e.printStackTrace();
                throw new InformationsException();
            }

            JSONObject payload = JWTService.getPayload(jws);
            int id = payload.getInt("id");

            // TODO : Verifier que l'utilisateur existe avant de verifier sont mot de passe : etre plus precis dans le renvoi d'erreur
            if(userRepository.verifyUserPassword(id, oldPassword) == null) throw new NotValidPasswordException();
            else{
                if(userRepository.setUserPassword(id, newPassword) == 0) throw new FailUpdatePasswordException();
            }
            ret.put("success", "Votre mot de passe à été changé avec succès");
        }catch (Exception e){
            ret.put("error", e.getMessage());
        }

        return ret.toString();
    }


    @PostMapping(path = "/updateScore")
    //TODO : verifier que le score est positif

    public String updateScore(@RequestBody String body_str){
        JSONObject body = new JSONObject(body_str);
        JSONObject ret = new JSONObject();

        try{
            String jws;
            int updateScore;
            try{
                jws = body.getString("jws");
                updateScore = body.getInt("xp");
            }catch (JSONException e){
                throw new InformationsException();
            }

            JSONObject payload = JWTService.getPayload(jws);
            int id = payload.getInt("id");
            Map<String, Integer> LevelAndXp = userRepository.getUserXpById(id);

            int level = LevelAndXp.get("level");
            int xp = LevelAndXp.get("xp");

            Map<String, Integer> newLevelXp = UserService.updateScore(level, xp, updateScore);
            userRepository.setUserLevelAndXp(id, newLevelXp.get("level"), newLevelXp.get("xp"));

        }catch (Exception e){
            ret.put("error", e.getMessage());
        }

        return  ret.toString();
    }

    @PostMapping(path = "/getUnlockedProducts")
    public String getUnlockedProducts(@RequestBody String body_str){
        JSONObject body = new JSONObject(body_str);
        JSONArray ret = new JSONArray();
        try {
            String jws;

            try {
                jws = body.getString("jws");
            } catch (JSONException e) {
                throw new InformationsException();
            }

            JSONObject payload = JWTService.getPayload(jws);
            int userId = payload.getInt("id");

            Iterator<Product> PossessedProducts = possessesRepository.findProductsByUserId(userId).iterator();

            while (PossessedProducts.hasNext()){
                ret.put(PossessedProducts.next().getInfos());
            }


        }catch (Exception e){
            JSONObject retError = new JSONObject();
            retError.put("error", e.getMessage());
            return retError.toString();
        }

        return ret.toString();
    }

}