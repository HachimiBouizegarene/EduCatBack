package org.hachimi.eduCat.repository.conjugation;

import org.hachimi.eduCat.entity.conjugation.SecondGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecondGroupRepository extends CrudRepository<SecondGroup, String> {
    @Query("SELECT sg.verb FROM SecondGroup sg ORDER BY RAND LIMIT 1")
    String findRandomVerb();
}
