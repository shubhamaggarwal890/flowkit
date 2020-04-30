package org.example.flowkit.jsonobject;

public class ActivityRequest {
    private String id;
    private String title;
    private String description;
    private Double offsetX;
    private Double offsetY;
    private String shape;
    private String predecessor;
    private String successor;
    private boolean auto;
    private RoleRequest role;
    private IndividualRequest individual;
    private AssociateRequest other;
    private boolean any_all;

    public ActivityRequest() {
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

    public RoleRequest getRole() {
        return role;
    }

    public void setRole(RoleRequest role) {
        this.role = role;
    }

    public IndividualRequest getIndividual() {
        return individual;
    }

    public void setIndividual(IndividualRequest individual) {
        this.individual = individual;
    }

    public AssociateRequest getOther() {
        return other;
    }

    public void setOther(AssociateRequest other) {
        this.other = other;
    }

    public boolean isAny_all() {
        return any_all;
    }

    public void setAny_all(boolean any_all) {
        this.any_all = any_all;
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
}
