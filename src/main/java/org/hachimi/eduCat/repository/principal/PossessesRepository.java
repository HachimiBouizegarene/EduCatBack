package org.hachimi.eduCat.repository.principal;

import org.hachimi.eduCat.entity.principal.Possesses;
import org.hachimi.eduCat.entity.principal.PossessesKey;
import org.hachimi.eduCat.entity.principal.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PossessesRepository extends CrudRepository<Possesses, PossessesKey> {

    @Query("SELECT p.product FROM Possesses p WHERE p.idUser = :idUser")
    public Iterable<Product> findProductsByUserId(@Param("idUser") Integer idUser);
}
