package org.hachimi.eduCat.repository.principal;

import org.hachimi.eduCat.entity.principal.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("SELECT p AS product , CASE WHEN pp.idUser IS NOT NULL THEN TRUE ELSE FALSE END AS possesses FROM Product p" +
            " LEFT JOIN p.possesses pp ON pp.idProduct = p.id AND pp.idUser = :id")
    public List<Map<String, Object>> findProductsAndUsedByIdUser(@Param("id") Integer id);


    @Query("SELECT p.price FROM Product p WHERE p.id = :id")
    public Integer findProductPriceById(@Param("id") Integer id);
    
}
