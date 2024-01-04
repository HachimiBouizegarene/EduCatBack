package org.hachimi.EduCat.controller;


import org.hachimi.EduCat.repository.DataService;
import org.springframework.web.bind.annotation.*;

@RestController
public class Home {
    private final DataService dataService;

    public Home(DataService dataService){
        this.dataService = dataService;
    }
    @GetMapping(path = "/test")
    public String test(){
        return "GOAT";
    }


}