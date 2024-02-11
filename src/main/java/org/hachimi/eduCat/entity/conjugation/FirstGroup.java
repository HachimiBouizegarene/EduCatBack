package org.hachimi.eduCat.entity.conjugation;


import jakarta.persistence.*;

@Entity
@Table(name = "premier_groupe")
public class FirstGroup {

    @Id
    @Column(name = "verbe")
    private String verb;

    public String getVerb(){
        return verb;
    }
}
