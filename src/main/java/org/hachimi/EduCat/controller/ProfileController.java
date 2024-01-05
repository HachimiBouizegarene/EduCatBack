package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.Exceptions.InformationsException;
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

@RestController
public class ProfileController {
    private final DataService dataService;
    public ProfileController(DataService dataService , JWTService jwtService){
        this.dataService = dataService;
    }
    @PostMapping(path = "/profile")
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
        }catch (Exception e){
            ret.put("error", e.getMessage());
            e.printStackTrace();
        }
        return ret.toString();
    }
}