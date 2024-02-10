package org.hachimi.eduCat.Exceptions;

public class NoVerbFoundException extends Exception{
    public NoVerbFoundException(){
        super("Aucun verbe n'a été trouvée");
    }
}
