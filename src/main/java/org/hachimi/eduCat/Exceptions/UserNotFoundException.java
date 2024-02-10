package org.hachimi.eduCat.Exceptions;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(){
        super("Adresse mail ou mot de passe incorrect");
    }
}

