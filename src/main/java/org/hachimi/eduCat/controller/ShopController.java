package org.hachimi.eduCat.controller;


import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotEnoughtEcats;
import org.hachimi.eduCat.Exceptions.ProductAlreadyPossessed;
import org.hachimi.eduCat.entity.principal.Possesses;
import org.hachimi.eduCat.entity.principal.PossessesKey;
import org.hachimi.eduCat.entity.principal.Product;
import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.repository.principal.PossessesRepository;
import org.hachimi.eduCat.repository.principal.ProductRepository;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.hachimi.eduCat.service.JWTService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ShopController {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    PossessesRepository possessesRepository;
    @Autowired
    UserRepository userRepository;
    @PostMapping(path = "/getProducts")
    public String getProducts(@RequestBody String body_str){
        JSONArray ret = new JSONArray();
        JSONObject body = new JSONObject(body_str);

        try{
            String jws = null;
            try{
                jws = body.getString("jws");
            }catch (JSONException e){
                throw new InformationsException();
            }

            int userId = JWTService.getPayload(jws).getInt("id");

            List<Map<String, Object>> products =  productRepository.findProductsAndUsedByIdUser(userId);

            for (Map<String, Object> row : products){
                Product product = (Product) row.get("product");
                boolean possesses = (Boolean) row.get("possesses");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", product.getInfos());
                jsonObject.put("possesses", possesses);
                ret.put(jsonObject);

            }

        }catch (Exception e){
            JSONObject errorRet = new JSONObject();
            errorRet.put("error", e.getMessage());

            e.printStackTrace();
            return errorRet.toString();
        }
        return ret.toString();

    }


    @PostMapping(path = "/getEcats")
    public String getEcats(@RequestBody String body_str){
        JSONObject body = new JSONObject(body_str);
        JSONObject ret = new JSONObject();
        try{
            String jws = null;
            try{
                jws = body.getString("jws");
            }catch (JSONException e){
                throw new InformationsException();
            }

            int idUser = JWTService.getPayload(jws).getInt("id");
            Integer userEcats = userRepository.findUserEcatsById(idUser);

            ret.put("ecats" , userEcats);

        }catch (Exception e){
            ret.put("error", e.getMessage());
            e.printStackTrace();
        }

        return  ret.toString();
    }

    @PostMapping(path = "/buyProduct")
    public String buyProduct(@RequestBody String body_str){
        JSONObject ret = new JSONObject();
        JSONObject body = new JSONObject(body_str);

        try{
            String jws = null;
            Integer idProduct;
            try{
                jws = body.getString("jws");
                idProduct = body.getInt("idProduct");
            }catch (JSONException e){
                throw new InformationsException();
            }

            int idUser = JWTService.getPayload(jws).getInt("id");

            Integer userEcats = userRepository.findUserEcatsById(idUser);

            Optional<Possesses> possessesExists = possessesRepository.findById(new PossessesKey(idUser, idProduct));
            if (possessesExists.isPresent()) throw new ProductAlreadyPossessed();

            Integer productPrice =productRepository.findProductPriceById(idProduct);

            if(userEcats <  productPrice) throw new NotEnoughtEcats();
            else{
                userRepository.setUserEcatsById(idUser, userEcats - productPrice);
                Possesses possesses = new Possesses(idProduct, idUser);
                possessesRepository.save(possesses);
            }


        }catch (Exception e){
            ret.put("error", e.getMessage());
            e.printStackTrace();
        }
        return ret.toString();
    }
}
