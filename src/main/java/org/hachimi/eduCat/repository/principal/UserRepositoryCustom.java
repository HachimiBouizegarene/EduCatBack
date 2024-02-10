package org.hachimi.eduCat.repository.principal;

import org.json.JSONObject;

public interface UserRepositoryCustom {
    public boolean setUserInformations(Integer id, JSONObject informations);
}
