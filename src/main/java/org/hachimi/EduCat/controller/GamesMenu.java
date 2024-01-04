package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.Entity.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.hachimi.EduCat.repository.DataService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GamesMenu {
    private final DataService dataService;

    public GamesMenu(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping(path = "/getGamesInfos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesInfos() {
        JSONArray gamesArray = dataService.getTableData("Jeu");
        return new ResponseEntity<>(gamesArray.toString(), HttpStatus.OK);
    }

    @PostMapping(path = "/getGamesImages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> getGamesImages(@RequestBody String body) {
        JSONObject json_body = new JSONObject(body);

        try {
            int IdJeu;
            IdJeu = json_body.getInt("IdJeu");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // Ajustez cela en fonction du type d'image
            byte[] imageBytes = dataService.getImageData(IdJeu); // Mettez en œuvre cette méthode pour obtenir les données d'image
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


}

