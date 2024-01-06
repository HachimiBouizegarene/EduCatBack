package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.Exceptions.InformationsException;
import org.hachimi.EduCat.repository.DataService;
import org.json.JSONArray;
import org.json.JSONException;
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
    public String getPartiesInfos(@RequestBody String body) throws InformationsException {
        JSONObject json_body = new JSONObject(body);
        JSONArray parties;
        String IdUser;

        try{
            IdUser = json_body.getString("IdUser");
            parties = dataService.getTableData("Partie", new JSONObject().put("IdUser", IdUser));
        }catch (JSONException e){
            throw new InformationsException();
        }

        return parties.toString();
    }
}
