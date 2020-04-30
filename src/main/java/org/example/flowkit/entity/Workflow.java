package org.example.flowkit.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Workflow {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)
    private String title;
    private String description;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Activity> activities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private Associates creator;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkflowInstance> workflowInstances = new ArrayList<>();

    private Integer deadlineDays;


    public Workflow() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Associates getCreator() {
        return creator;
    }

    public void setCreator(Associates creator) {
        this.creator =creator;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<WorkflowInstance> getWorkflowInstances() {
        return workflowInstances;
    }

    public void setWorkflowInstances(List<WorkflowInstance> workflowInstances) {
        this.workflowInstances = workflowInstances;
    }

    public Integer getDeadlineDays() {
        return deadlineDays;
    }

    public void setDeadlineDays(Integer deadlineDays) {
        this.deadlineDays = deadlineDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
