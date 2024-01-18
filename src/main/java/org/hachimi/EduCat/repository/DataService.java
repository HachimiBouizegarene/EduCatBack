package org.hachimi.EduCat.repository;

import org.hachimi.EduCat.Entity.User;
import org.hachimi.EduCat.Exceptions.FailUpdatePasswordException;
import org.hachimi.EduCat.Exceptions.ServerException;
import org.hachimi.EduCat.Exceptions.UserNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class DataService {
    private final String databaseUrl;
    private final String databasePassword;

    private final String databaseUserName;



    Connection connection = null;
    public DataService( @Value("${database.url}") String databaseUrl,
                        @Value("${database.username}") String databaseUserName,
                        @Value("${database.password}") String databasePassword) {

        this.databaseUrl = databaseUrl;
        this.databaseUserName = databaseUserName;
        this.databasePassword = databasePassword;
        try{
            connection = dataBaseConnection();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Connection dataBaseConnection() throws SQLException{
        return DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword);
    }

    public JSONObject insertUser(User user) throws ServerException {
        JSONObject ret = new JSONObject();
        try{
            String sql = "INSERT INTO `utilisateur` (`IdUser`, `Nom`, `Pseudonyme` , `Prenom`, `Email`, `Classe` , `MotDePasse`)" +
                    " VALUES (NULL, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPseudo());
            pstmt.setString(3, user.getForename());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getClasse());
            pstmt.setString(6, user.getPasseword());
            int rs = pstmt.executeUpdate();
            if (rs <= 0) ret.put("error", "Error when access to DataBase");
        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerException();
        }

        return ret;
    }

    public JSONObject getUser(String user_mail, String user_password,Integer id) throws ServerException, UserNotFoundException{
        JSONObject ret = new JSONObject();
        PreparedStatement pstmt;
        try{
            String sql;
            if(id == null){
                sql = "SELECT * FROM utilisateur WHERE Email = ? AND MotDePasse = ?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, user_mail);
                pstmt.setString(2, user_password);
            }else{
                sql = "SELECT * FROM utilisateur WHERE IdUser = ?";
                pstmt = connection.prepareStatement(sql);
                pstmt.setInt(1, id);
            }
            ResultSet resultSet = pstmt.executeQuery();
            int rows_count = 0;
            if(resultSet.next()){
                rows_count ++ ;
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String ColumnType = metaData.getColumnTypeName(i);
                    if(ColumnType.equals("BLOB") || ColumnType.equals("MEDIUMBLOB")){
                        try {
                            Blob blob = (Blob) resultSet.getBlob(columnName);
                            byte[] data = blob.getBytes(1,(int) blob.length());
                            ret.put(columnName, data);
                        }catch (Exception e){}

                    }else {
                        ret.put(columnName, resultSet.getString(columnName));
                    }
                }
            }
            if(rows_count <= 0 ) throw new UserNotFoundException();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new ServerException();
        }
        return ret;
    }
    public JSONArray getTableData(String table, JSONObject... filters) {

        // EXEMPLE D'UTILISATION AVEC FILTRES : getTableData("Partie", new JSONObject().put("IdUser", "1")); (récupères toutes parties ou IdUser = 1)
        // A optimiser

            String sql = "SELECT * FROM " + table;

            if(filters.length > 0){

                sql = "SELECT * FROM " + table + " WHERE ";

                for(int i = 0 ; i < filters.length ; ++i){
                    String columnName = filters[i].keys().next();
                    String filterValue = (String) filters[i].get(columnName);

                    sql += columnName + " = '" + filterValue + "' AND ";
                }

                sql = sql.substring(0, sql.length() - 5);
            }

        JSONArray jsonArray =  new JSONArray();
        try {

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                JSONObject json = new JSONObject();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String ColumnType = metaData.getColumnTypeName(i);
                    if(ColumnType.equals("BLOB") || ColumnType.equals("MEDIUMBLOB")){
                        Blob blob = (Blob) resultSet.getBlob(columnName);
                        if (blob != null) {
                            byte[] data = blob.getBytes(1,(int) blob.length());
                            json.put(columnName, data);
                        }else {
                            json.put(columnName, "null");
                        }
                    }else {
                        json.put(columnName, resultSet.getString(columnName));
                    }
                }
                jsonArray.put(json);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getProfileImage(){
        JSONArray jsonArray =  new JSONArray();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT PhotoProfil FROM utilisateur");
            while (resultSet.next()) {
                JSONObject json = new JSONObject();
                json.put("PhotoProfil", resultSet.getBlob("PhotoProfil"));
                jsonArray.put(json);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONObject insertPartie(String ScorePartie, String LibelleDifficultePartie, int IdUser, int IdJeu) throws ServerException {
        JSONObject ret = new JSONObject();
        try {
            String sql = "INSERT INTO `partie` (`ScorePartie`, `LibelleDifficultePartie`, `IdUser`, `IdJeu`) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            // Assuming the JSON object contains the necessary fields for Partie entity
            pstmt.setString(1, ScorePartie);
            pstmt.setString(2, LibelleDifficultePartie);
            pstmt.setInt(3, IdUser);
            pstmt.setInt(4, IdJeu);

            int rs = pstmt.executeUpdate();
            if (rs <= 0) {
                ret.put("error", "Error when accessing the database for inserting Partie");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new ServerException();
        }

        return ret;
    }

    public String getLibelleMatiereById(int idMatiere) throws ServerException {
        String sql = "SELECT NomMatiere from matiere WHERE IdMatiere = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, idMatiere);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Retourner l'objet JSON sous forme de chaîne
                return resultSet.getString("NomMatiere");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServerException();

        }

        return "Matière Inconnue";
    }

    public String getLibelleJeuById(int idJeu) throws ServerException {
        String sql = "SELECT NomJeu from jeu WHERE IdJeu = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, idJeu);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Retourner l'objet JSON sous forme de chaîne
                return resultSet.getString("NomJeu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServerException();
        }

        return "Nom du jeu inconnu";
    }

    public int getIdJeuByLibelle(String NomJeu) throws ServerException {
        String sql = "SELECT IdJeu from jeu WHERE NomJeu = ?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, NomJeu);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                return resultSet.getInt("IdJeu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServerException();
        }

        return -1;
    }

    public void updateUser(int id, Iterator<String> columns, JSONObject data) throws ServerException {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("UPDATE utilisateur SET ");
        while(columns.hasNext()){
            String column = columns.next();
            sqlBuilder.append(column);
            if(columns.hasNext()) sqlBuilder.append(" = ? , ");
            else sqlBuilder.append(" = ? ");
        }
        sqlBuilder.append(" WHERE IdUser = ?");
        String sql = sqlBuilder.toString();
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql); System.out.println(sqlBuilder.toString());
            int index = 1;
            for (String key : data.keySet()){
                if (key.equals("PhotoProfil")){
                    JSONArray array = data.getJSONArray(key);
                    byte [] bytes = new byte[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        bytes[i]=(byte)(((int)array.get(i)));
                    }
                    pstmt.setBytes(index, bytes);
                }else{
                    pstmt.setString(index, data.getString(key));
                }
                index += 1;
            }
            pstmt.setInt(index, id);
            int changes = pstmt.executeUpdate();
            System.out.println(changes);
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new ServerException();
        }

    }

    public boolean verifyPassword(int id,String password) throws ServerException, UserNotFoundException {
        String sql = "SELECT MotDePasse FROM utilisateur WHERE IdUser = ?";
        boolean ret = false;
        try{
            String get_password = null;
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if(resultSet.next())  {
                get_password = resultSet.getString(1);
                ret =  get_password.equals(password);
            }else{
                throw new UserNotFoundException();
            }

        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerException();
        }
        return ret;
    }

    public boolean upadtePassword(int id, String password) throws ServerException, FailUpdatePasswordException {
        String sql = "UPDATE utilisateur SET MotDePasse = ? WHERE IdUser = ? ";
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, password);
            pstmt.setInt(2, id);
            int rows_updated = pstmt.executeUpdate();
            if (rows_updated < 1 ) return false;
            else return true;
        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerException();
        }
    }


}
