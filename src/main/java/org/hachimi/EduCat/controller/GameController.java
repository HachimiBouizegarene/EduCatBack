package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.Exceptions.NotValidJwsException;
import org.hachimi.EduCat.Exceptions.ServerException;
import org.hachimi.EduCat.repository.DataService;
import org.hachimi.EduCat.service.JWTService;
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
}
