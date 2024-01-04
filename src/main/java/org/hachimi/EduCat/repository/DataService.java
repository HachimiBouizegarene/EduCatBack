package org.hachimi.EduCat.repository;

import org.hachimi.EduCat.Entity.User;
import org.hachimi.EduCat.Exceptions.ServerException;
import org.hachimi.EduCat.Exceptions.UserNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
            String sql = "INSERT INTO `utilisateur` (`IdUser`, `Nom`, `Prenom`, `Email`, `Classe` , `MotDePasse`)" +
                    " VALUES (NULL, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getForename());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getClasse());
            pstmt.setString(5, user.getPasseword());
            int rs = pstmt.executeUpdate();
            if (rs <= 0) ret.put("error", "Error when access to DataBase");
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new ServerException();
        }

        return ret;
    }

    public JSONObject getUser(String user_mail, String user_password) throws ServerException, UserNotFoundException{
        JSONObject ret = new JSONObject();
        try{
            String sql = "SELECT * FROM utilisateur WHERE Email = ? AND MotDePasse = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user_mail);
            pstmt.setString(2, user_password);
            ResultSet resultSet = pstmt.executeQuery();
            int rows_count = 0;
            if(resultSet.next()){
                rows_count ++ ;
                ResultSetMetaData metaData = resultSet.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    ret.put(columnName, resultSet.getString(columnName));
                }
            }
            if(rows_count <= 0 ) throw new UserNotFoundException();
        }catch (SQLException e){
            System.out.println(e.getMessage());
            throw new ServerException();
        }
        return ret;
    }

    public JSONArray getTableData(String table) {
        JSONArray jsonArray =  new JSONArray();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                JSONObject json = new JSONObject();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    if(!columnName.equals("ImageJeu"))
                        json.put(columnName, resultSet.getString(columnName));
                }
                jsonArray.put(json);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return jsonArray;
    }

    public byte[] getImageData(int idJeu) {
        try {
            String query = "SELECT ImageJeu FROM jeu WHERE IdJeu = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, idJeu);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    Blob blob = resultSet.getBlob("ImageJeu");
                    if (blob != null) {
                        // Convertir le Blob en tableau d'octets
                        return blob.getBytes(1, (int) blob.length());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new byte[0]; // Retourne un tableau vide si l'image n'est pas trouvée ou en cas d'erreur
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
}
