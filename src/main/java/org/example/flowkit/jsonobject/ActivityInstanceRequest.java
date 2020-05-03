package org.example.flowkit.jsonobject;

public class ActivityInstanceRequest {
    private String id;
    private String title;
    private String remark;
    private String associate;
    private String predecessor;
    private String successor;
    private DocumentRequest document;

    public ActivityInstanceRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getAssociate() {
        return associate;
    }

    public void setAssociate(String associate) {
        this.associate = associate;
    }

    public String getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }

    public String getSuccessor() {
        return successor;
    }

    public void setSuccessor(String successor) {
        this.successor = successor;
    }

    public DocumentRequest getDocument() {
        return document;
    }

    public void setDocument(DocumentRequest document) {
        this.document = document;
    }
}
