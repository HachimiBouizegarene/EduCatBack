package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotValidJwsException;
import org.hachimi.eduCat.Exceptions.ServerException;
import org.hachimi.eduCat.entity.principal.Defi;
import org.hachimi.eduCat.entity.principal.ParticipationDefi;
import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.repository.principal.DefiRepository;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.hachimi.eduCat.service.JWTService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DefisController {
    @Autowired
    private DefiRepository defiRepository;

    @Autowired
    UserRepository userRepository;

    @PostMapping(path = "/getDefis", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDefis() throws ServerException {

        JSONArray ret = new JSONArray();

        Iterable<Defi> defis = defiRepository.findAll();

        for (Defi defi : defis) {
            JSONObject defiInfos = defi.GetInfos();

            ret.put(defiInfos);
        }

        return ret.toString();
    }

    @PostMapping(path = "/getDefisJoueur", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDefisJoueur(@RequestBody String body_str) throws ServerException, InformationsException {

        JSONObject body = new JSONObject(body_str);
        JSONArray ret = new JSONArray();
        try {
            String jws = body.getString("jws");
            JWTService.verifyJWT(jws);
            JSONObject payload = JWTService.getPayload(jws);
            Optional<User> userOptional = userRepository.findById(payload.getInt("id"));

            if (!userOptional.isPresent()) {
                throw new NotValidJwsException();
            }

            User user = userOptional.get();
            // Tout les defis du joueur
            List<ParticipationDefi> defisJoueur = defiRepository.findAllByJoueurAssocie(user.getId());

            for (ParticipationDefi participationDefi : defisJoueur) {
                Optional<Defi> defiOptional = defiRepository.findById(participationDefi.getDefiAssocie().getId());

                if (defiOptional.isPresent()) {
                    Defi defi = defiOptional.get();
                    JSONObject defiInfos = defi.GetInfos();

                    defiInfos.put("statut", participationDefi.getStatut());

                    if(defi.getJeuAssocie() != null)
                        defiInfos.put("infosJeuAssocie", defi.getJeuAssocie().GetInfos());

                    ret.put(defiInfos);
                }
            }

        } catch (JSONException e) {
            throw new InformationsException();
        } catch (Exception e) {
            ret.put("error");
            e.printStackTrace();
        }

        return ret.toString();
    }
}
