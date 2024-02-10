package org.hachimi.eduCat.repository.principal;

import jakarta.transaction.Transactional;
import org.hachimi.eduCat.entity.principal.UserRemake;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends CrudRepository<UserRemake, Integer>, UserRepositoryCustom {
    @Query("SELECT u.Id FROM UserRemake u WHERE u.password = :password AND u.email = :email")
    Integer findUserIdByMailPassword(@Param("email") String email, @Param("password") String password);



    @Query("SELECT True FROM UserRemake u where u.id = :id AND u.password = :password")
    Boolean verifyUserPassword(@Param("id") Integer id, @Param("password") String password);

    @Transactional
    @Modifying()
    @Query("UPDATE UserRemake u SET u.password = :password WHERE u.id = :id")
    Integer setUserPassword(@Param("id") Integer id, @Param("password") String password);
}
