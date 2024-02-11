package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotValidJwsException;
import org.hachimi.eduCat.Exceptions.ServerException;
import org.hachimi.eduCat.entity.principal.Game;
import org.hachimi.eduCat.entity.principal.GameSession;
import org.hachimi.eduCat.entity.principal.Subject;
import org.hachimi.eduCat.repository.principal.GameRepository;
import org.hachimi.eduCat.repository.principal.GameSessionRepository;
import org.hachimi.eduCat.repository.principal.SubjectRepository;
import org.hachimi.eduCat.service.JWTService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class GamesController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private SubjectRepository subjectRepository;


    @Autowired
    GameSessionRepository gameSessionRepository;


    @PostMapping(path = "/getGamesInfos", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getGamesInfos() throws ServerException {

        JSONArray ret = new JSONArray();

        Iterable<Game> games = gameRepository.findAll();

        for (Game game : games) {
            JSONObject gameInfos = game.GetInfos();
            Optional<Subject> subject =  subjectRepository.findById(game.getId());
            if (subject.isPresent()){
                gameInfos.put("subjectName",subject.get().getName());
            }

            ret.put(gameInfos);
        }

        return ret.toString();
    }

    @PostMapping(path = "/insertPartie", produces = MediaType.APPLICATION_JSON_VALUE)
    public String insertPartie(@RequestBody String body) throws NotValidJwsException, ServerException {
        JSONObject jsonBody = new JSONObject(body);
        JSONObject ret = new JSONObject();

        try{
            String jws = jsonBody.getString("jws");
            int userId = JWTService.getPayload(jws).getInt("id");

            String gameName = jsonBody.getString("name");
            int gameId = gameRepository.getIdGameByName(gameName);
            String gameSessionScore = jsonBody.getString("score");
            String difficultyLibelle = jsonBody.getString("difficultyLibelle");

            GameSession gameSession = new GameSession(gameSessionScore, difficultyLibelle, userId, gameId);
            gameSessionRepository.save(gameSession);

        }catch (JSONException e){
            ret.put("error", new InformationsException().getMessage());
        }
        catch (Exception e){
            ret.put("error", e.getMessage());
        }


        return ret.toString();
    }
}

