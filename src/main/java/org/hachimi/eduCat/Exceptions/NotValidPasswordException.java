package org.hachimi.eduCat.Exceptions;

public class NotValidPasswordException extends  Exception{
    public NotValidPasswordException(){
        super("Le mot de passe est incorrect");
    }
}
