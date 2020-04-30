package org.example.flowkit.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Individual {
    private @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    private String vertical;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associate")
    private Associates associates;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "particularIndividual")
    private List<Activity> activities = new ArrayList<>();

    public Individual() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVertical() {
        return vertical;
    }

    public void setVertical(String vertical) {
        this.vertical = vertical;
    }

    public Associates getAssociates() {
        return associates;
    }

    public void setAssociates(Associates associates) {
        this.associates = associates;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
