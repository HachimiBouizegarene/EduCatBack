package org.hachimi.eduCat.entity.principal;


import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.List;

@Entity
@Table(name = "produit")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contenu")
    private byte[] content;
    @Column(name = "categorie")
    private String category;


    @Column(name = "prix")
    private Integer price;

    @Column(name = "nom")
    private String name;

    @OneToMany(mappedBy = "product")
    private List<Possesses> possesses;

    @OneToMany(mappedBy = "product")
    private List<User> users;



    public JSONObject getInfos(){
        JSONObject ret = new JSONObject();
        ret.put("content", content);
        ret.put("id", id);
        ret.put("category", category);
        ret.put("name", name);
        ret.put("price", price);
        return ret;
    }

    public byte[] getContent() {
        return this.content;
    }
}
