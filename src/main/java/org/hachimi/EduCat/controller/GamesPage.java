package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.Exceptions.ServerException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.hachimi.EduCat.repository.DataService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GamesPage {
    private final DataService dataService;

    public GamesPage(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping(path = "/getGamesInfos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGamesInfos() throws ServerException {

        JSONArray jeux = dataService.getTableData("Jeu");

        for (int i = 0; i < jeux.length() ; i++) {
            int idJeu = ((JSONObject) jeux.get(i)).getInt("IdMatiere");
            ((JSONObject) jeux.get(i)).put("NomMatiere", dataService.getLibelleMatiereById(idJeu));
        }

        return jeux.toString();
    }
}

