package org.example.flowkit.jsonobject;

import java.util.ArrayList;
import java.util.List;

public class WorkflowRequest {
    private String title;
    private String description;
    private Integer deadline;
    private AssociateRequest creator;
    private List<ActivityRequest> activities = new ArrayList<>();

    public WorkflowRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }

    public List<ActivityRequest> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityRequest> activities) {
        this.activities = activities;
    }

    public AssociateRequest getCreator() {
        return creator;
    }

    public void setCreator(AssociateRequest creator) {
        this.creator = creator;
    }
}
