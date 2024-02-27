package org.hachimi.eduCat.entity.principal;

import jakarta.persistence.*;
import org.json.JSONObject;

@Entity
@Table(name = "defiparticipation")
public class ParticipationDefi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdParticipationDefi")
    private Integer id;

    @Column(name = "Statut")
    private Integer statut;

    @Column(name = "DateParticipation")
    private String dateParticipation;

    @Column(name = "IdUser")
    private Integer joueurAssocie;

    @ManyToOne
    @JoinColumn(name = "IdDefi", referencedColumnName = "IdDefi")
    private Defi defiAssocie;

    public Integer getId() {
        return id;
    }

    public JSONObject GetInfos() {
        JSONObject ret = new JSONObject();
        ret.put("id", id);
        ret.put("statut", statut);
        ret.put("dateParticipation", dateParticipation);
        ret.put("joueurAssocie", joueurAssocie);
        ret.put("DefiAssocie", this.defiAssocie.getId());

        return ret;
    }

    public Defi getDefiAssocie() {
        return defiAssocie;
    }

    public Integer getStatut() {
        return statut;
    }

    public void setDefiAssocie(Defi defiAssocie) {
        this.defiAssocie = defiAssocie;
    }


}
