package org.hachimi.eduCat.entity.principal;


import jakarta.annotation.Generated;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.hachimi.eduCat.service.GeneralService;
import org.hibernate.annotations.ColumnDefault;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "partie")
public class GameSession {


    public GameSession(){}
    public GameSession(String score, String difficultyLibelle, Integer idUser, Integer idGame){
        this.score = score;
        this.difficultyLibelle = difficultyLibelle;
        this.idUser = idUser;
        this.idGame = idGame;
        Date date = new Date();
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpartie")
    private Integer id;

    @Column(name = "scorepartie")
    private String score;

    @Column(name = "datepartie")
    private String date;

    @Column(name = "libelledifficultepartie")
    private String difficultyLibelle;

    @Column(name = "iduser")
    private Integer idUser;

    @Column(name = "idjeu")
    private Integer idGame;

    public Integer getIdGame() {
        return idGame;
    }

    public JSONObject GetInfos(){

        JSONObject ret = new JSONObject();

        ret.put("id", id);
        ret.put("score", score);
        ret.put("date", date);
        ret.put("difficultyLibelle", difficultyLibelle);
        ret.put("idUser", idUser);
        ret.put("idGame", idGame);

        return ret;
    }
}
