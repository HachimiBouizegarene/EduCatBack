package org.hachimi.eduCat.Exceptions;

public class NotValidJwsException extends Exception{
    public NotValidJwsException(){
        super("Le jws n'est pas valide");
    }
}
