package org.hachimi.EduCat.Exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("Adresse mail ou mot de passe incorrect");
    }
}

