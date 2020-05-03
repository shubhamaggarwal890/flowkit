package org.example.flowkit.jsonobject;

import java.util.ArrayList;
import java.util.List;

public class ActivityInstanceResponse {
    private String id;
    private String title;
    private String description;
    private Double offsetX;
    private Double offsetY;
    private String shape;
    private String predecessor;
    private String successor;
    private boolean auto;
    private String status;
    private DocumentRequest document;
    private List<ActivityAssociatesResponse> associates = new ArrayList<>();
    private boolean any_all;

    public ActivityInstanceResponse() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(Double offsetX) {
        this.offsetX = offsetX;
    }

    public Double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(Double offsetY) {
        this.offsetY = offsetY;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
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

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DocumentRequest getDocument() {
        return document;
    }

    public void setDocument(DocumentRequest document) {
        this.document = document;
    }

    public List<ActivityAssociatesResponse> getAssociates() {
        return associates;
    }

    public void setAssociates(List<ActivityAssociatesResponse> associates) {
        this.associates = associates;
    }

    public boolean isAny_all() {
        return any_all;
    }

    public void setAny_all(boolean any_all) {
        this.any_all = any_all;
    }
}
