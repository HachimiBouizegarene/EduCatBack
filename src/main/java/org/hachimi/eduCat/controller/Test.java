package org.hachimi.eduCat.controller;



import org.hachimi.eduCat.repository.conjugation.FirstGroupRepository;
import org.hachimi.eduCat.repository.principal.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {


    @Autowired
    private FirstGroupRepository firstGroupRepository;

    @Autowired
    private UserRepository userRepository;
    @GetMapping(path = "/")
    public String test(){


//        User user = userRepository.findAll().iterator().next();
//        return user.getEmail();
//        Map<String, Integer> test = userRepository.getUserXpById(1);
//
//        return test.get("level").toString();

//        userRepository.setUserLevelAndXp(1, 3, 3);
//        return "true";

        return  firstGroupRepository.findRandomVerb();


    }
}
