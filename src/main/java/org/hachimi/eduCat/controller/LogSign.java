package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.ServerException;
import org.hachimi.eduCat.Exceptions.UserNotFoundException;
import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.entity.principal.UserRemake;
import org.hachimi.eduCat.repository.DataService;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.hachimi.eduCat.service.JWTService;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class LogSign {

    private final DataService dataService;

    @Autowired
    private UserRepository userRepository;

    public LogSign(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody String body){
        JSONObject ret = new JSONObject();
        JSONObject json_body = new JSONObject(body);
        try{
            String mail;
            String password;
            try{
                mail = json_body.getString("email");
                password = json_body.getString("password");
            }catch (JSONException e){
                throw new InformationsException();
            }
            if(mail == "" || password == "") throw new InformationsException();
            JSONObject payloadUser = new JSONObject();
            Integer userId = userRepository.findUserIdByMailPassword(mail, password);
            if(userId == null ) throw new UserNotFoundException();
            payloadUser.put("id", userId);
            payloadUser.put("cdate" , System.currentTimeMillis());
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
            // GET INFORMATIONS
            String name = json_body.getString("name");
            String forename = json_body.getString("forename");
            String email = json_body.getString("email");
            String password = json_body.getString("password");
            String classe = json_body.getString("classe");
            String pseudo = json_body.getString("pseudo");


            //TODO : Gerer les cas de duplications (pseudo, email)
            //TODO : Gerer les cas de Foreign Key
            try{
                UserRemake user = new UserRemake(name, forename, email, password, classe, pseudo);
                userRepository.save(user);
            }catch (DataAccessException e) {
                throw new ServerException();
            };

        } catch (Exception e){
            ret.put("error", e.getMessage());
        }
        return ret.toString();
    }
}
