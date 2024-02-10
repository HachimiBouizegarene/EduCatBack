package org.hachimi.eduCat.repository;

import org.hachimi.eduCat.entity.principal.User;
import org.hachimi.eduCat.Exceptions.EmailPseudoDuplicate;
import org.hachimi.eduCat.Exceptions.FailUpdatePasswordException;
import org.hachimi.eduCat.Exceptions.ServerException;
import org.hachimi.eduCat.Exceptions.UserNotFoundException;
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

    public JSONObject getLevelXp(int id) throws ServerException{
        JSONObject ret = new JSONObject();
        String sql = "SELECT Xp, Niveau FROM utilisateur WHERE idUser = ?";
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            if(!resultSet.next()) throw new ServerException();
            ret.put("xp", resultSet.getInt("Xp"));
            ret.put("level", resultSet.getInt("Niveau"));
        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerException();
        }
        return ret;
    }

    public boolean updateLevelXp(int id, JSONObject newLevelXp) throws ServerException{
        JSONObject ret = new JSONObject();
        String sql = "UPDATE utilisateur SET Xp = ? , Niveau = ?  WHERE IdUser = ?";
        try{
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, newLevelXp.getInt("newXp"));
            pstmt.setInt(2, newLevelXp.getInt("newLevel"));
            pstmt.setInt(3, id);
            int result = pstmt.executeUpdate();
            if(result < 1) return false;
        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerException();
        }
        return true;
    }


}
