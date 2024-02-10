package org.hachimi.eduCat.Exceptions;

public class MailFormatException extends Exception{
    public MailFormatException(){
        super("Le format du mail est incorrect");
    }
}
