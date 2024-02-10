package org.hachimi.eduCat.repository.principal;

import org.hachimi.eduCat.entity.principal.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game , Integer> {
}
