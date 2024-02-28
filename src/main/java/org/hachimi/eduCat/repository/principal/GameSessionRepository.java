package org.hachimi.eduCat.repository.principal;

import jakarta.transaction.Transactional;
import org.hachimi.eduCat.entity.principal.GameSession;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, Integer> {

    @Query("SELECT gs FROM GameSession gs WHERE gs.idUser = :idUser")
    Iterable<GameSession> getGameSessionsByIdUser(@Param("idUser") Integer idUser);

    @Query("SELECT COUNT(gs) FROM GameSession gs WHERE gs.idUser = :idUser AND gs.idGame = :idGame")
    Integer countGamesByUserIdAndGameId(@Param("idUser") Integer idUser, @Param("idGame") Integer idGame);

}
