package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.service.ConjugaisonService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Conjugation {

    private final ConjugaisonService conjugaisonService;
    @Autowired
    public Conjugation(ConjugaisonService conjugaisonService){
        this.conjugaisonService= conjugaisonService;
    }

    @PostMapping(path = "/conjugation")
    public String conjugaison(@RequestBody String body){
        JSONObject ret = new JSONObject();
        JSONObject json_body = new JSONObject(body);
        try{
            String group;
            try{
                int difficulty = json_body.getInt("difficulty");
                JSONObject verb_data = conjugaisonService.generateVerb(difficulty);
                for (String key : verb_data.keySet()){
                    ret.put(key , verb_data.get(key));
                }
            }catch (JSONException e){
                throw new InformationsException();
            }
        }catch (Exception e){
            ret.put("error", e.getMessage());
            e.printStackTrace();
        }
        return  ret.toString();
    }
}