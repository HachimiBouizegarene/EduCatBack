package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.ServerException;
import org.hachimi.eduCat.entity.principal.Game;
import org.hachimi.eduCat.entity.principal.Subject;
import org.hachimi.eduCat.repository.principal.GameRepository;
import org.hachimi.eduCat.repository.principal.SubjectRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.hachimi.eduCat.repository.DataService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.Optional;

@RestController
public class GamesPage {
    private final DataService dataService;
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public GamesPage(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping(path = "/getGamesInfos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGamesInfos() throws ServerException {

           JSONArray ret = new JSONArray();

//        JSONArray jeux = dataService.getTableData("Jeu");

        // A FAIRE
        Iterable<Game> games = gameRepository.findAll();

        for (Game game : games) {
            JSONObject gameInfos = game.GetInfos();
            Optional<Subject> subject =  subjectRepository.findById(game.getId());
            if (subject.isPresent()){
                gameInfos.put("subjectName",subject.get().getName());
            }

            ret.put(gameInfos);
        }
        // FIN A FAIRE

//        for (int i = 0; i < jeux.length() ; i++) {
//            int idJeu = ((JSONObject) jeux.get(i)).getInt("IdMatiere");
//            ((JSONObject) jeux.get(i)).put("NomMatiere", dataService.getLibelleMatiereById(idJeu));
//        }

        return ret.toString();
    }
}

