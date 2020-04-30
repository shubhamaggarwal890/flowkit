package org.example.flowkit.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Roles {
    private @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<Associates> associates = new ArrayList<>();

    public Roles() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Associates> getAssociates() {
        return associates;
    }

    public void setAssociates(List<Associates> associates) {
        this.associates = associates;
    }

}
