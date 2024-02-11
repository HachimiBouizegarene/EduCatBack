package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotValidJwsException;
import org.hachimi.eduCat.entity.principal.GameSession;
import org.hachimi.eduCat.repository.principal.GameRepository;
import org.hachimi.eduCat.repository.principal.GameSessionRepository;
import org.hachimi.eduCat.service.JWTService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

@RestController
public class StatisticsPage {
    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private  GameRepository gameRepository;



    @PostMapping(path = "/getPartiesInfos")
    public String getPartiesInfos(@RequestBody String body) throws InformationsException, NotValidJwsException {
        JSONObject ret = new JSONObject();
        JSONArray data = new JSONArray();
        try {
            JSONObject json_body = new JSONObject(body);

            String jws = json_body.getString("jws");

            JSONObject payload = JWTService.getPayload(jws);

            Integer id = payload.getInt("id");

            Iterable<GameSession> gameSessions = gameSessionRepository.getGameSessionsByIdUser(id);
            Iterator<GameSession> gameSessionsIerator = gameSessions.iterator();
            int i = 0;
            while (gameSessionsIerator.hasNext() && i <= 100){
                i = i + 1 ;
                GameSession gameSession = gameSessionsIerator.next();
                JSONObject gameSessionJSON = gameSession.GetInfos();
                int idGame = gameSession.getIdGame();
                String gameLibelle = gameRepository.getNameGameById(idGame);
                gameSessionJSON.put("gameName" , gameLibelle);
                data.put(gameSessionJSON);
            }
            ret.put("data", data);
        } catch (Exception e) {
            ret.put("error", e.getMessage());
            e.printStackTrace();
        }
        return ret.toString();
    }

}
