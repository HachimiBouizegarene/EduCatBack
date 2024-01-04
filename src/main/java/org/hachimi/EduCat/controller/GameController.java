package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.Entity.User;
import org.hachimi.EduCat.Exceptions.ServerException;
import org.hachimi.EduCat.repository.DataService;
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
    public ResponseEntity<JSONObject> insertPartie(@RequestBody String body) {
        JSONObject json_body = new JSONObject(body);

        try {
            JSONObject result = dataService.insertPartie(
                    json_body.getString("ScorePartie"),
                    json_body.getString("LibelleDifficultePartie"),
                    json_body.getInt("IdUser"),
                    json_body.getInt("IdJeu")
            );

            return ResponseEntity.ok(result);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JSONObject().put("error", "Server error"));
        }
    }
}
