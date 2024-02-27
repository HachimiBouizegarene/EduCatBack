package org.hachimi.eduCat.repository.principal;

import org.hachimi.eduCat.entity.principal.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface SubjectRepository extends CrudRepository<Subject, Integer> {


    //@Query("SELECT s FROM Subject s JOIN s.")
    //public Iterable<Subject> findSubjectByUserId(@Param("id") Integer id);
}
