package org.hachimi.eduCat.service;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserService {


    public static Integer level0Xp = 100;

    public static Integer gapXpLevel = 20;
    public static Integer getPercentage(Integer level, Integer actualXp){
        Float xpStaying = (float) getXpStaying(level, actualXp);
        Float xpToAttemp = (float) (level0Xp + (level - 1) * gapXpLevel);
        return (int) ((1- xpStaying / xpToAttemp) * 100);
    }


    public static Integer getXpStaying(Integer level, Integer actualXp){
        Integer xpStaying = ((level0Xp + (level - 1) * gapXpLevel) - actualXp);
        return xpStaying;
    }



    public static Map<String, Integer> updateScore(Integer level, Integer actualXp, Integer xpToAdd){
        Map<String, Integer> ret = new HashMap<>();
        while(true){
            Integer xpStaying = getXpStaying(level, actualXp);
            Integer delta = xpStaying - xpToAdd;
            if(delta > 0){
                actualXp += xpToAdd;
                break;
            }else{
                level+=1;
                actualXp = 0 ;
                xpToAdd = delta * -1;
            }
        }
        ret.put("xp", actualXp);
        ret.put("level", level);
        return ret;
    }
}