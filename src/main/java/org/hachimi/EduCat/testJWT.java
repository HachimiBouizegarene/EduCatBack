package org.hachimi.EduCat;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.hachimi.EduCat.service.JWTService;
import org.json.JSONObject;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class testJWT {

    public static void main(String args[]){
        String jwt = JWTService.generateJWT(new JSONObject("{'id': 3, 'hey' : 'goa'}"));
        System.out.println(jwt);

        JSONObject ret = JWTService.getPayload(jwt);
        System.out.println(ret.toString());


    }
}
