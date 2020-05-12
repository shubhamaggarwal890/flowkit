package org.example.flowkit.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "workflow_instance")
public class WorkflowInstance {
    private @Id @GeneratedValue Long id;

    @ManyToOne
    @JoinColumn(name = "workflow")
    private Workflow workflow;

    @Column(nullable = false)
    private String title;

    private String description;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date instance_date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator")
    private Associates initiator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer")
    private Associates workflowFor;

    @OneToMany(mappedBy = "workflowInstance", fetch = FetchType.LAZY)
    private List<ActivityInstance> activityInstances = new ArrayList<>();

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }


    public Long getId() {
        return id;
    }

    public Associates getInitiator() {
        return initiator;
    }

    public void setInitiator(Associates initiator) {
        this.initiator = initiator;
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

    public List<ActivityInstance> getActivityInstances() {
        return activityInstances;
    }

    public void setActivityInstances(List<ActivityInstance> activityInstances) {
        this.activityInstances = activityInstances;
    }

    public Associates getWorkflowFor() {
        return workflowFor;
    }

    public void setWorkflowFor(Associates workflowFor) {
        this.workflowFor = workflowFor;
    }

    public Date getInstance_date() {
        return instance_date;
    }

    public void setInstance_date(Date instance_date) {
        this.instance_date = instance_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
