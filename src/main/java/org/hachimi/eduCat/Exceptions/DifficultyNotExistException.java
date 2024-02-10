package org.hachimi.eduCat.Exceptions;

public class DifficultyNotExistException extends  Exception{
    public DifficultyNotExistException(int difficulty){
        super("La difficulte " +difficulty+  " n'existe pas");
    }
}