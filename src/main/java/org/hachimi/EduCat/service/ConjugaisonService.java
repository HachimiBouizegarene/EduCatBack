package org.hachimi.EduCat.service;

import org.hachimi.EduCat.Exceptions.GroupConjugationException;
import org.hachimi.EduCat.Exceptions.NoVerbFoundException;
import org.hachimi.EduCat.Exceptions.ServerException;
import org.hachimi.EduCat.Exceptions.TimeConjugationException;
import org.hachimi.EduCat.repository.DataServiceConjugaison;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class ConjugaisonService {
    private final DataServiceConjugaison dataServiceConjugaison;
    ArrayList<String> times = new ArrayList<>(Arrays.asList("present", "imparfait", "futur_simple", "passe_simple"));
    ArrayList<String> groups = new ArrayList<>(Arrays.asList("premier_groupe", "second_groupe"));
    ArrayList<String> pronoms = new ArrayList<>(Arrays.asList("je", "tu", "il/elle", "bous", "vous", "ils/elles"));
    @Autowired
    public ConjugaisonService(DataServiceConjugaison dataServiceConjugaison) {
        this.dataServiceConjugaison = dataServiceConjugaison;
    }


    public JSONObject generateVerb(String group, String time) throws TimeConjugationException, GroupConjugationException, ServerException, NoVerbFoundException {
        if(!VerifyTime(time)) throw new TimeConjugationException();
        if(!VerifyGroup(group)) throw new GroupConjugationException();
        String verb  = dataServiceConjugaison.get_verb(group).getString("verbe");

        Random random = new Random();
        int random_int = random.nextInt(pronoms.size());
        String pronom = pronoms.get(random_int);

        String conjuged_verb = conjugVerb(pronom, verb, time, group);
        JSONObject ret = new JSONObject();
        ret.put("verb", verb);
        ret.put("pronom", pronom);
        ret.put("time", time);
        ret.put("conjuged_verb", conjuged_verb);
        return ret;
    }

    public String conjugVerb(String pronom , String verb, String time,String  group){
        String conjugaison = null;
        if(group == "premier_groupe"){
            String substring = verb.substring(0, verb.length() - 2);
            switch (pronom) {
                case "je", "il/elle":conjugaison = substring + "e"; break;
                case "tu" :conjugaison = substring + "es"; break;
                case "nous" :conjugaison = substring + "ons"; break;
                case "vous" :conjugaison = substring + "ez"; break;
                case "ils/elles" :conjugaison = substring + "ent"; break;
            }
        }
        System.out.println(conjugaison);
        return conjugaison;
    }

    public boolean VerifyTime(String time) {
        return  times.contains(time);
    }

    public boolean VerifyGroup(String time) {
        return  groups.contains(time);
    }
}
