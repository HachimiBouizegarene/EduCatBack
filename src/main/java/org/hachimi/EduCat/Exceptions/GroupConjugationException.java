package org.hachimi.EduCat.Exceptions;

public class GroupConjugationException extends Exception{
    public GroupConjugationException(){
        super("Le groupe de verbes n'existe pas ou n'est pas pris en charge");
    }
}
