package org.hachimi.eduCat.repository.principal;

import org.hachimi.eduCat.entity.principal.Possesses;
import org.hachimi.eduCat.entity.principal.PossessesKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PossessesRepository extends CrudRepository<Possesses, PossessesKey> {

}
