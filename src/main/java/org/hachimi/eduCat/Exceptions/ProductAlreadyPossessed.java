package org.hachimi.eduCat.Exceptions;

public class ProductAlreadyPossessed extends Exception{
    public ProductAlreadyPossessed(){
        super("Vous possedez déjà ce produit.");
    }
}
