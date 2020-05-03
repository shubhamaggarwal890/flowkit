package org.example.flowkit.jsonobject;

public class WorkflowInstanceResponse {
    private Long id;
    private String title;
    private String description;
    private String wk_description;
    private String date;
    private String deadline;
    AssociateRequest initiator;

    public WorkflowInstanceResponse() {
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
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

    public String getWk_description() {
        return wk_description;
    }

    public void setWk_description(String wk_description) {
        this.wk_description = wk_description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public AssociateRequest getInitiator() {
        return initiator;
    }

    public void setInitiator(AssociateRequest initiator) {
        this.initiator = initiator;
    }
}
