package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.NotValidJwsException;
import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.Exceptions.FailUpdatePasswordException;
import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotValidPasswordException;
import org.hachimi.eduCat.entity.principal.UserRemake;
import org.hachimi.eduCat.repository.DataService;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.hachimi.eduCat.service.JWTService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Iterator;
import java.util.Optional;

@RestController
public class ProfileController {
    @Autowired
    UserRepository userRepository;
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
            Optional<UserRemake> userOptional = userRepository.findById(payload.getInt("id"));

            if (userOptional.isPresent()){
                UserRemake user  = userOptional.get();
                ret = user.GetInfos(false);
            }else{
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
            ret.put("success","Informations personnels mises a jours !");
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


}