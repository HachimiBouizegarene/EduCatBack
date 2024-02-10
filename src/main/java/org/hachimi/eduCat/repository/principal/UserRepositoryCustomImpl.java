package org.hachimi.eduCat.repository.principal;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.hachimi.eduCat.service.GeneralService;
import org.json.JSONObject;

import java.util.Iterator;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @Transactional
    public boolean setUserInformations(Integer id, JSONObject informations){

        // TODO : Deleguer a une autre classe la verification des informations

        StringBuilder jpql = new StringBuilder("UPDATE UserRemake u SET");
        Iterator<String> keys =  informations.keys();

        // SI AUCUNE INFO
        if (!keys.hasNext()) return true;

        boolean first = true;
        while (keys.hasNext()){
            String key = keys.next();
            if(!first)jpql.append(",");
            else first= false;
            jpql.append(" u.");
            jpql.append(key);
            jpql.append(" = :");
            jpql.append(key);
        };



        jpql.append(" WHERE u.id = :id");
        System.out.println(jpql.toString());
        Query query =  entityManager.createQuery(jpql.toString());
        query.setParameter("id", id);
        keys = informations.keys();

        while (keys.hasNext()){
            String key = keys.next();
            if (key.equals("profileImage")) {
                query.setParameter(key, GeneralService.JSONarrayToBytes(informations.getJSONArray("profileImage")));
            }else query.setParameter(key, informations.getString(key));

        }
        int updateResult = query.executeUpdate();
        return updateResult != 0;
    }
}
