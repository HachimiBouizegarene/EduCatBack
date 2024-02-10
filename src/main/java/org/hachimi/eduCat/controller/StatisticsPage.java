package org.hachimi.eduCat.controller;

import org.hachimi.eduCat.Exceptions.InformationsException;
import org.hachimi.eduCat.Exceptions.NotValidJwsException;
import org.hachimi.eduCat.repository.DataService;
import org.hachimi.eduCat.service.JWTService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsPage {

    private final DataService dataService;

    public StatisticsPage(DataService dataService){
        this.dataService = dataService;
    }

    @PostMapping(path = "/getPartiesInfos")
    public String getPartiesInfos(@RequestBody String body) throws InformationsException, NotValidJwsException {
        try {
            JSONObject json_body = new JSONObject(body);
            JSONArray parties = new JSONArray();

            String jws = json_body.getString("jws");

            JSONObject payload = JWTService.getPayload(jws);

            parties = dataService.getTableData("Partie", new JSONObject().put("IdUser", payload.getString("id")));

            for (int i = 0; i < parties.length(); i++) {
                if (i > 100) {
                    break;  // Casse la boucle si i > 100
                }

                int idJeu = ((JSONObject) parties.get(i)).getInt("IdJeu");
                ((JSONObject) parties.get(i)).put("NomJeu", dataService.getLibelleJeuById(idJeu));
            }

            return parties.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
