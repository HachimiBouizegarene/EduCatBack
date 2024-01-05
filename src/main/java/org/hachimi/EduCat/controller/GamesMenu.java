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
    public String getGamesInfos() {
        return dataService.getTableData("Jeu").toString();
    }
}

