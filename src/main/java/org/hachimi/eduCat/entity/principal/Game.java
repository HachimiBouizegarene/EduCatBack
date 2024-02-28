package org.hachimi.eduCat.entity.principal;


import jakarta.persistence.*;
import org.json.JSONObject;

@Entity
@Table(name = "jeu")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idjeu")
    private Integer id;

    @Column(name = "nomjeu")
    private String name;


    @Column(name = "imagejeu")
    private byte[] image;

    @Column(name = "idtype")
    private Integer idType;

    @Column(name = "idmatiere")
    private Integer idSubject;


    public Integer getId(){
        return id;
    }

    public JSONObject GetInfos(){
        JSONObject ret = new JSONObject();
        ret.put("name", name);
        ret.put("idType", idType);
        ret.put("idSubject", idSubject);
        ret.put("image", image);
        ret.put("id", id);

        return ret;
    }

    public String getNomJeu(){
        return name;
    }

    public byte[] getImageJeu(){
        return image;
    }
}
