package org.hachimi.eduCat.repository.conjugation;


import org.hachimi.eduCat.entity.conjugation.FirstGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FirstGroupRepository extends CrudRepository<FirstGroup, String> {

    @Query("SELECT fg.verb FROM FirstGroup fg ORDER BY RAND LIMIT 1")
    String findRandomVerb();

}
