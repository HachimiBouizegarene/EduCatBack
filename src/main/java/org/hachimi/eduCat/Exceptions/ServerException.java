package org.hachimi.eduCat.Exceptions;

public class ServerException extends Exception{
    public ServerException(){
        super("Le serveur a rencontré un problème, ressayez plus tard");
    }
}
