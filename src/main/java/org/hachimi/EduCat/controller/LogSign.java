package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.Entity.User;
import org.hachimi.EduCat.Exceptions.InformationsException;
import org.hachimi.EduCat.repository.DataService;
import org.hachimi.EduCat.service.JWTService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogSign {

    private final DataService dataService;

    public LogSign(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody String body){
        JSONObject ret = new JSONObject();
        JSONObject json_body = new JSONObject(body);

        try{
            String user_mail;
            String user_password;
            try{
                user_mail = json_body.getString("user_email");
                user_password = json_body.getString("user_password");
            }catch (JSONException e){
                throw new InformationsException();
            }
            if(user_mail == "" || user_password == "") throw new InformationsException();

            JSONObject payloadUser = new JSONObject();
            JSONObject json_user_data = dataService.getUser(user_mail, user_password);
            String idUser = json_user_data.getString("IdUser");
            payloadUser.put("id", idUser);
            String jws = JWTService.generateJWT(payloadUser);
            ret.put("jws" , jws);

        }catch (Exception e){
            ret.put("error", e.getMessage());
        }
        return ret.toString();
    }

    @PostMapping(path = "/signin")
    public  String signin(@RequestBody String body){
        JSONObject ret = new JSONObject();
        JSONObject json_body = new JSONObject(body);
        try{
            User user = new User(json_body);
            ret = dataService.insertUser(new User(json_body));
        }catch (Exception e){
            ret.put("error", e.getMessage());
        }
        return ret.toString();
    }
}
