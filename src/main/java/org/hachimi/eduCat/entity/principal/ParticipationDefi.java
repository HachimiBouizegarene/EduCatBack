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
    private Integer statut  = 0;

    @Column(name = "IdUser")
    private Integer joueurAssocie;

    @Column(name = "NbPartiesCondition")
    private Integer nbParties;

    @Column(name = "ScoreCondition")
    private Integer scoreCondition;

    @ManyToOne
    @JoinColumn(name = "IdJeu", referencedColumnName = "Idjeu", nullable = true)
    private Game jeuAssocie;

    @Column(name = "RecompenseXP")
    private Integer recompenseXP;

    @Column(name = "RecompenseECats")
    private Integer recompenseECats;

    @Column(name = "DifficulteDefi")
    private Double difficulteDefi;

    public Integer getId() {
        return id;
    }

    public JSONObject GetInfos() {
        JSONObject ret = new JSONObject();
        ret.put("id", id);
        ret.put("statut", statut);
        ret.put("joueurAssocie", joueurAssocie);
        ret.put("scoreCondition", scoreCondition);
        ret.put("nbParties", nbParties);
        ret.put("difficulteDefi", difficulteDefi);
        ret.put("recompenseECats", recompenseECats);
        ret.put("recompenseXP", recompenseXP);

        return ret;
    }

    public Integer getStatut() {
        return statut;
    }

    public Game getJeuAssocie() {
        return jeuAssocie;
    }

    public Integer getScoreCondition(){
        return scoreCondition;
    }

    public Integer getNbParties(){
        return nbParties;
    }

    public void setJoueurAssocie(Integer joueurAssocie) {
        this.joueurAssocie = joueurAssocie;
    }

    public void setNbParties(Integer nbParties) {
        this.nbParties = nbParties;
    }

    public void setScoreCondition(Integer scoreCondition) {
        this.scoreCondition = scoreCondition;
    }

    public void setJeuAssocie(Game jeuAssocie) {
        this.jeuAssocie = jeuAssocie;
    }

    public void setRecompenseECats(Integer recompenseECats) {
        this.recompenseECats = recompenseECats;
    }

    public void setDifficulteDefi(Double difficulteDefi) {
        this.difficulteDefi = difficulteDefi;
    }

    public void setRecompenseXp(Integer recompenseXP) {
        this.recompenseXP = recompenseXP;
    }
}
