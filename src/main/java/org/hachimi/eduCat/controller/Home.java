package org.hachimi.eduCat.controller;


import org.hachimi.eduCat.entity.principal.Possesses;
import org.hachimi.eduCat.entity.principal.Product;
import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.repository.principal.PossessesRepository;
import org.hachimi.eduCat.repository.principal.ProductRepository;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Iterator;

@RestController
public class Home {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PossessesRepository possessesRepository;

    @GetMapping(path = "/test")
    public String test() throws SQLException {
        Iterable<User> users = userRepository.findUsersTest();
        Iterator<User> it = users.iterator();
        String ret = it.next().GetInfos(false).toString();
        System.out.println(it.hasNext());
        return ret;


    }


}