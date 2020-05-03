package org.example.flowkit.jsonobject;

import java.util.ArrayList;
import java.util.List;

public class WorkflowInstanceRequest {
    private Long id;
    private String title;
    private String description;
    private Long creator;
    private String customer;
    private List<ActivityInstanceRequest> activities = new ArrayList<>();

    public WorkflowInstanceRequest() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public List<ActivityInstanceRequest> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityInstanceRequest> activities) {
        this.activities = activities;
    }
}
