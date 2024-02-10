package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotValidJwsException;
import org.hachimi.eduCat.Exceptions.ServerException;
import org.hachimi.eduCat.repository.DataService;
import org.hachimi.eduCat.service.JWTService;
import org.hachimi.eduCat.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private final DataService dataService;

    public GameController(DataService dataService) {
        this.dataService = dataService;
    }

    @PostMapping(path = "/insertPartie", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JSONObject> insertPartie(@RequestBody String body) throws NotValidJwsException, ServerException {
        JSONObject json_body = new JSONObject(body);

        int IdUser = Integer.parseInt(JWTService.getPayload(json_body.getString("jws")).get("id").toString());
        int IdJeu = dataService.getIdJeuByLibelle(json_body.getString("NomJeu"));

        try {
            JSONObject result = dataService.insertPartie(
                    json_body.getString("ScorePartie"),
                    json_body.getString("LibelleDifficultePartie"),
                    IdUser,
                    IdJeu
            );
        } catch (ServerException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JSONObject().put("error", "Server error"));
    }

    @PostMapping(path = "/updateScore")
    //verifier que le score est positif
    public String updateScore(@RequestBody String body_str){
        JSONObject body = new JSONObject(body_str);
        JSONObject ret = new JSONObject();

        try{
            String jws;
            int updateScore;
            try{
                jws = body.getString("jws");
                updateScore = body.getInt("xp");
            }catch (JSONException e){
                throw new InformationsException();
            }

            JSONObject payload = JWTService.getPayload(jws);
            int id = payload.getInt("id");
            JSONObject LevelXp = dataService.getLevelXp(id);
            int level = LevelXp.getInt("level");
            int xp = LevelXp.getInt("xp");

            JSONObject newLevelXp = UserService.updateScore(level, xp, updateScore);

            dataService.updateLevelXp(id, newLevelXp);

        }catch (Exception e){
            ret.put("error", e.getMessage());
        }

        return  ret.toString();
    }
}
