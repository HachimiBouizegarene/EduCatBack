package org.hachimi.EduCat.Exceptions;

public class TimeConjugationException extends Exception{
    public TimeConjugationException(String time){
        super("Le temps de conjugaison " + time + " n'existe pas ou n'est pas pris en charge");
    }

}