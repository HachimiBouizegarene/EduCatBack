package org.hachimi.EduCat.Entity;

import org.hachimi.EduCat.Exceptions.InformationsException;
import org.hachimi.EduCat.Exceptions.MailFormatException;
import org.hachimi.EduCat.Exceptions.ServerException;
import org.hachimi.EduCat.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private String name;
    private String forename;
    private String  classe;
    private String email;
    private String password;
    private String pseudo;

    private byte[] image;

    private Integer level;
    private Integer xp;


    public User(String name, String forename, String classe, String email, String password, String pseudo, byte[] image, Integer level, Integer xp) {
        this.name = name;
        this.forename = forename;
        this.classe = classe;
        this.email = email;
        this.password = password;
        this.pseudo = pseudo;
        this.image = image;
        this.xp = xp;
        this.level =  level;
    }

    public User(JSONObject infos) throws InformationsException,ServerException, MailFormatException  {
        try{
            this.name = infos.getString("user_name");
            this.forename = infos.getString("user_forename");
            this.classe = infos.getString("user_classe");
            this.email = infos.getString("user_email");
            this.password = infos.getString("user_password");
            this.pseudo = infos.getString("user_pseudo");
        }catch (JSONException e) {
            throw new ServerException();
        }
        if (this.name == "" || this.forename == "" || this.classe == "" || this.email == "" ||
                this.password == "" ){
            throw new InformationsException();
        }

        if(!isValidEmail(this.email)){
            throw new MailFormatException();
        }
    }
    public static boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+[.][A-Za-z]{2,3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public JSONObject getInfos(){
        JSONObject ret = new JSONObject();
        ret.put("Nom", this.name);
        ret.put("Prenom", this.forename);
        ret.put("Email", this.email);
        ret.put("MotDePass", this.password);
        ret.put("Classe", this.classe);
        ret.put("Pseudonyme", this.pseudo);
        ret.put("PhotoProfil", this.image);
        ret.put("Niveau", this.level);
        ret.put("Pourcentage", UserService.getPercentage(this.level, this.xp));
        return ret;
    }

    public String getName() {
        return name;
    }

    public String getForename() {
        return forename;
    }

    public String getClasse() {
        return classe;
    }

    public String getEmail() {
        return email;
    }

    public String getPasseword() {
        return password;
    }

    public String getPseudo() {return  pseudo;}

//    public Integer getLevel() {
//        return this.level;
//    }
}
