package org.hachimi.eduCat.repository.principal;

import org.hachimi.eduCat.entity.principal.Game;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game , Integer> {

    @Query("SELECT g.id FROM Game g WHERE g.name = :name")
    Integer getIdGameByName(@Param("name") String name);


    @Query("SELECT g.name FROM Game g WHERE g.id = :id")
    String getNameGameById(@Param("id") Integer id);
}
