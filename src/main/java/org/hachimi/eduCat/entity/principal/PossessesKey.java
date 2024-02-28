package org.hachimi.eduCat.entity.principal;

import java.io.Serializable;

public class PossessesKey {

    public PossessesKey(){};
    public PossessesKey(Integer idUser, Integer idProduct){
        this.idProduct = idProduct;
        this.idUser= idUser;

    }
    private  Integer idUser;
    private Integer idProduct;
}
