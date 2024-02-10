package org.hachimi.eduCat.Exceptions;

public class EmailPseudoDuplicate extends Exception{
    public EmailPseudoDuplicate(){
        super("Email ou pseudonyme déjà utilisé");
    }
}
