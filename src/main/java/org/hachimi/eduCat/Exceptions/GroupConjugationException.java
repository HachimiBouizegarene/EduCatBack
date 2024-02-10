package org.hachimi.eduCat.Exceptions;

public class GroupConjugationException extends Exception{
    public GroupConjugationException(String group){
        super("Le groupe de verbes " + group + " n'existe pas ou n'est pas pris en charge");
    }
}