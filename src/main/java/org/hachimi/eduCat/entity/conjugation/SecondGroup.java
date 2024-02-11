package org.hachimi.eduCat.entity.conjugation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "second_groupe")
public class SecondGroup {


    @Id
    @Column(name = "verbe")
    private String verb;

    public String getVerb(){
        return verb;
    }
}
