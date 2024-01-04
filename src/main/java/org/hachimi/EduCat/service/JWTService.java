package org.hachimi.EduCat.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;

@Service
public class JWTService {
    private static SecretKey key;

    static  {
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode("O2t5H8dj9SBPykRf2VdmVzUNblFsI1ksEtIwWIJcBS8="));
        System.out.println(Base64.getEncoder().encodeToString(key.getEncoded()));
    }

    public static String generateJWT (JSONObject payload){
        String jwt = Jwts.builder().content(payload.toString()).signWith(key).compact();
        return jwt;
    }



    public static JSONObject getInfos(String jwt_str){
        Jwt<?, ?> jwt = null;
        try{
            jwt = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt_str);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new JSONObject(jwt);
    }

//    public static boolean verifyJWT(String jws_str) {
//        boolean ret ;
//        try {
//            Jwt<?, ?> jws = Jwts.parser().verifyWith(key).build().parseSignedClaims(jws_str);
//            ret = true;
//
//        } catch (Exception e) {
//            ret = false;
//        }
//        return ret;
//    }

    public static JSONObject getPayload(String jws_str){
        JSONObject ret = new JSONObject();
        Claims content = null;
        try{
            content = Jwts.parser().verifyWith(key).build().parseSignedClaims(jws_str).getPayload();
        }catch (Exception e){
            ret.put("error", e.getMessage());
        }
        ret.put("payload", new JSONObject(content));
        return ret;
    }
}
