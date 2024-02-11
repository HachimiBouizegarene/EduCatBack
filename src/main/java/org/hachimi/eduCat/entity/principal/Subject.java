package org.hachimi.eduCat.entity.principal;


import jakarta.persistence.*;

@Entity
@Table(name = "matiere")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmatiere")
    private Integer id;

    @Column(name = "nommatiere")
    private String name;

    public String getName() {
        return name;
    }
}
