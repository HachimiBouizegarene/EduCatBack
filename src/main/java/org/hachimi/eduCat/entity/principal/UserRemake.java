package org.hachimi.eduCat.entity.principal;


import jakarta.persistence.*;
import org.hachimi.eduCat.service.GeneralService;
import org.hachimi.eduCat.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.SQLException;
import java.sql.Struct;

@Entity
@Table(name = "utilisateur")
public class UserRemake {

    public UserRemake(){
    };
    public UserRemake(String name, String forename, String email, String password, String classe, String pseudo){
        this.name = name;
        this.forename = forename;
        this.email = email;
        this.password = password;
        this.classe = classe;
        this.pseudo = pseudo;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser")
    private Integer id;

    @Column(name= "nom")
    private String name;

    @Column(name = "prenom")
    private String forename;

    @Column(name = "email")
    private String email;

    @Column(name = "classe")
    private String classe;

    @Column(name = "motdepasse")
    private String password;

    @Column(name = "photoprofil")
    private byte[] profileImage;

    @Column(name = "Pseudonyme")
    private String pseudo;

    @Column(name = "Xp")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int xp;

    @Column(name = "niveau")
    private int level;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getForename() {
        return forename;
    }

    public String getEmail() {
        return email;
    }

    public String getClasse() {
        return classe;
    }

    public String getPassword() {
        return password;
    }

    public byte[] getProfileImage() throws SQLException {
        // A changer si trop grosse image ne va pas fonctionner
//        byte[] data = profile_image.getBytes(1, (int) profile_image.length());
        return profileImage;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public JSONObject GetInfos(boolean withPassword) throws SQLException {
        JSONObject ret = new JSONObject();
        ret.put("id", getId());
        ret.put("pseudo", getPseudo());
        ret.put("name", getName());
        ret.put("forename", getForename());
        ret.put("profileImage", getProfileImage());
        ret.put("classe", getClasse());
        ret.put("level", getLevel());
        ret.put("xp", getXp());
        ret.put("email", getEmail());
        ret.put("percentage", UserService.getPercentage(this.level, this.xp));
        if(withPassword) ret.put("password", getPassword());
        return ret;

    }

    public String getPseudo() {
        return pseudo;
    }
}
