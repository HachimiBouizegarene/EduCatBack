package org.hachimi.eduCat.controller;



import org.hachimi.eduCat.entity.principal.Product;
import org.hachimi.eduCat.repository.conjugation.FirstGroupRepository;
import org.hachimi.eduCat.repository.principal.ProductRepository;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class Test {


    @Autowired
    private FirstGroupRepository firstGroupRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping(path = "/")
    public String test(){

    return "cac";
    }
}
