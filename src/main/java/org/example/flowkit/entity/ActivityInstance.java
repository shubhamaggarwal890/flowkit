package org.example.flowkit.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "activity_instance")
public class ActivityInstance implements Serializable {

    private @Id @GeneratedValue Long id;

    private String title;
    private String remark;

    // ACCEPT, REJECT, PENDING
    private String status;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "predecessor")
    private ActivityInstance source;

    @OneToOne(mappedBy = "source", fetch = FetchType.LAZY)
    private ActivityInstance predecessor;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "successor")
    private ActivityInstance destination;

    @OneToOne(mappedBy = "destination", fetch = FetchType.LAZY)
    private ActivityInstance successor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document")
    private Document document;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_instance")
    private WorkflowInstance workflowInstance;

    @OneToMany(mappedBy = "activity_instance_associate", fetch = FetchType.LAZY)
    private List<ActivityAssociates> activityInstanceAssociates = new ArrayList<>();

    public ActivityInstance() {
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public WorkflowInstance getWorkflowInstance() {
        return workflowInstance;
    }

    public void setWorkflowInstance(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
    }

    public List<ActivityAssociates> getActivityInstanceAssociates() {
        return activityInstanceAssociates;
    }

    public void setActivityInstanceAssociates(List<ActivityAssociates> activityInstanceAssociates) {
        this.activityInstanceAssociates = activityInstanceAssociates;
    }

    public ActivityInstance getSource() {
        return source;
    }

    public void setSource(ActivityInstance source) {
        this.source = source;
    }

    public ActivityInstance getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(ActivityInstance predecessor) {
        this.predecessor = predecessor;
    }

    public ActivityInstance getDestination() {
        return destination;
    }

    public void setDestination(ActivityInstance destination) {
        this.destination = destination;
    }

    public ActivityInstance getSuccessor() {
        return successor;
    }

    public void setSuccessor(ActivityInstance successor) {
        this.successor = successor;
    }

}
