package org.hachimi.eduCat.entity.principal;


import jakarta.persistence.*;
import org.json.JSONObject;
import org.springframework.context.annotation.Primary;

@Entity
@IdClass(PossessesKey.class)
@Table(name = "possede")
public class Possesses {

    public Possesses(){};

    public Possesses(Integer idProduct, Integer idUser){
        this.idProduct = idProduct;
        this.idUser = idUser;
    }


    @Id
    @Column(name = "idUtilisateur")
    private Integer idUser;


    @Id
    @Column(name = "idProduit")
    private Integer idProduct;

    @ManyToOne
    @MapsId("idProduct")
    @JoinColumn(name = "idUtilisateur")
    private User user;

    @ManyToOne
    @MapsId("idUser")
    @JoinColumn(name = "idProduit")
    private Product product;


    public JSONObject getInfos(){
        JSONObject ret = new JSONObject();
        ret.put("idUser", idUser);
        ret.put("idProduct", idProduct);
        return ret;
    }

}
