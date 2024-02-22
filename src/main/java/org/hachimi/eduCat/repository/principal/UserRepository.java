package org.hachimi.eduCat.repository.principal;

import jakarta.transaction.Transactional;
import org.hachimi.eduCat.entity.principal.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;


@Repository
public interface UserRepository extends CrudRepository<User, Integer>, UserRepositoryCustom {
    @Query("SELECT u.Id FROM User u WHERE u.password = :password AND u.email = :email")
    Integer findUserIdByMailPassword(@Param("email") String email, @Param("password") String password);



    @Query("SELECT True FROM User u where u.id = :id AND u.password = :password")
    Boolean verifyUserPassword(@Param("id") Integer id, @Param("password") String password);

    @Transactional
    @Modifying()
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :id")
    Integer setUserPassword(@Param("id") Integer id, @Param("password") String password);

    @Query("SELECT u.xp AS xp, u.level AS level FROM User u WHERE u.id = :id")
    Map<String, Integer> getUserXpById(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.level = :level, u.xp = :xp WHERE u.id = :id")
    Integer setUserLevelAndXp(@Param("id") Integer id, @Param("level") Integer level,@Param("xp") Integer xp);

    // Méthode pour rechercher un utilisateur par son adresse e-mail
    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    // Méthode pour vérifier si un utilisateur existe en fonction de son adresse e-mail
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :email")
    boolean existsByEmail(@Param("email") String email);
}
