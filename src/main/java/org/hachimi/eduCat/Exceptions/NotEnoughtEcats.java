package org.hachimi.eduCat.Exceptions;

public class NotEnoughtEcats extends Exception{
    public NotEnoughtEcats(){
        super("Le nombre de eCats possédées est insuffisant.");
    }
}
