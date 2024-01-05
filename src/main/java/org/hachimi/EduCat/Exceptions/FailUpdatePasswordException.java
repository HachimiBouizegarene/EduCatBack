package org.hachimi.EduCat.Exceptions;

public class FailUpdatePasswordException extends Exception{
    public FailUpdatePasswordException(){
        super("Echec lors du changement de mot de passe");
    }
}
