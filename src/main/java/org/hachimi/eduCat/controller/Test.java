package org.hachimi.eduCat.controller;



import org.hachimi.eduCat.entity.principal.UserRemake;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {


    @Autowired
    private UserRepository userRepository;
    @GetMapping(path = "/")
    public String test(){
        UserRemake user = userRepository.findAll().iterator().next();
        return user.getEmail();
    }
}
