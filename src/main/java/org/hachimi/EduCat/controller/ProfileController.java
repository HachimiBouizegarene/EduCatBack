package org.hachimi.EduCat.controller;

import org.hachimi.EduCat.repository.DataService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Blob;
import java.sql.SQLException;

@RestController
public class ProfileController {

    private final DataService dataService;

    public ProfileController(DataService dataService){
        this.dataService = dataService;
    }
    @PostMapping(path = "/profile")
    public String profile() throws SQLException {
        JSONArray ret = dataService.getTableData("utilisateur");
//        System.out.println(ret.getJSONObject(0).get("PhotoProfil"));
        return "hey";
    }
}