package org.hachimi.eduCat.controller;


import org.hachimi.eduCat.repository.principal.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Home {

    @Autowired
    UserRepository userRepository;


    @GetMapping(path = "/test")
    public String test(){
        JSONObject up = new JSONObject();
        up.put("id", 4);
        up.put("name", "SALUTATION");
        up.put("pseudo", "baka");
//        boolean test = userRepository.setUserInformations(up);
        return "true";
    }


}