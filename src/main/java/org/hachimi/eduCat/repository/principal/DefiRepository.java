package org.hachimi.eduCat.repository.principal;

import org.hachimi.eduCat.entity.principal.ParticipationDefi;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefiRepository extends CrudRepository<ParticipationDefi, Integer> {

    @Query("SELECT pd FROM ParticipationDefi pd WHERE pd.joueurAssocie = :joueurId")
    List<ParticipationDefi> findAllByJoueurAssocie(@Param("joueurId") Integer joueurId);

}
