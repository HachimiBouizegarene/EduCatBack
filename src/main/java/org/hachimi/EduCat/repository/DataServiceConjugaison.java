package org.hachimi.EduCat.repository;

import org.hachimi.EduCat.Exceptions.GroupConjugationException;
import org.hachimi.EduCat.Exceptions.NoVerbFoundException;
import org.hachimi.EduCat.Exceptions.ServerException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

@Repository
public class DataServiceConjugaison {
    private final String databaseUrl;
    private final String databasePassword;
    private final String databaseUserName;

    Connection connection = null;
    public DataServiceConjugaison( @Value("${database.conjugaison.url}") String databaseUrl,
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

    public Connection dataBaseConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl, databaseUserName, databasePassword);
    }

    public JSONObject get_verb(String group) throws ServerException, NoVerbFoundException {
        JSONObject ret = new JSONObject();
        try{
            String sql = "SELECT verbe FROM " + group +  " ORDER BY RAND() LIMIT 1;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
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
            if(rows_count <= 0 ) throw new NoVerbFoundException();

        }catch (SQLException e){
            e.printStackTrace();
            throw new ServerException();
        };
        return ret;
    }


}
