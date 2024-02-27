package org.hachimi.eduCat.entity.principal;

import jakarta.persistence.*;
import org.json.JSONObject;

@Entity
@Table(name = "defi")
public class Defi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdDefi")
    private Integer id;

    @Column(name = "Description")
    private String description;

    @Column(name = "RecompenseXP")
    private Integer recompenseXP;

    @Column(name = "RecompenseECat")
    private Integer recompenseEcat;

    @ManyToOne
    @JoinColumn(name = "IdJeu", referencedColumnName = "idjeu", nullable = true)
    private Game jeuAssocie;

    public Integer getId() {
        return id;
    }

    public JSONObject GetInfos() {
        JSONObject ret = new JSONObject();
        ret.put("id", id);
        ret.put("description", description);
        ret.put("recompenseXP", recompenseXP);
        ret.put("recompenseEcat", recompenseEcat);

        return ret;
    }

    public Game getJeuAssocie() {
        return jeuAssocie;
    }
}
