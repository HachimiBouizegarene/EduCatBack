package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.Exceptions.FailUpdatePasswordException;
import org.hachimi.EduCat.Exceptions.InformationsException;
import org.hachimi.EduCat.Exceptions.NotValidPasswordException;
import org.hachimi.EduCat.repository.DataService;
import org.hachimi.EduCat.service.JWTService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

@RestController
public class ProfileController {
    private final DataService dataService;
    public ProfileController(DataService dataService , JWTService jwtService){
        this.dataService = dataService;
    }
    @PostMapping(path = "/getProfile")
    public String profile(@RequestBody String body_str) {
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
            ret = dataService.getUser(null, null,  payload.getInt("id"));
            ret.remove("MotDePasse");
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
            Iterator<String> keys_iterator = body.keys();
            JSONObject payload = JWTService.getPayload(jws);
            dataService.updateUser(payload.getInt("id"), keys_iterator, body);
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
            String old_password;
            String new_password;
            try{
                jws = body.getString("jws");
                JWTService.verifyJWT(jws);
                body.remove("jws");
                old_password = body.getString("old_password");
                new_password = body.getString("new_password");
            }catch (JSONException e){
                e.printStackTrace();
                throw new InformationsException();
            }

            JSONObject payload = JWTService.getPayload(jws);
            int id = payload.getInt("id");
            if(!dataService.verifyPassword(id, old_password)) throw new NotValidPasswordException();
            else{
                if(!dataService.upadtePassword(id, new_password)) throw new FailUpdatePasswordException();
            }
            ret.put("success", "Votre mot de passe à été changé avec succès");
        }catch (Exception e){
            ret.put("error", e.getMessage());
        }

        return ret.toString();
    }


}