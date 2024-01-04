package org.hachimi.EduCat.Exceptions;

public class TimeConjugationException extends Exception{
    public TimeConjugationException(){
        super("Le temps de conjugaison n'existe pas ou n'est pas pris en charge");
    }

}
