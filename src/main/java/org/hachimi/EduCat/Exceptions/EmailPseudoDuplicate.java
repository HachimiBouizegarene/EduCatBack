package org.hachimi.EduCat.Exceptions;

public class EmailPseudoDuplicate extends Exception{
    public EmailPseudoDuplicate(){
        super("Email ou pseudonyme déjà utilisé");
    }
}
