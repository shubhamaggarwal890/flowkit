package org.example.flowkit.entity;

import javax.persistence.*;

@Entity
public class Activity {

    private @Id
    @GeneratedValue
    Long id;
    private String name;
    private String description;
    private Double offsetX;
    private Double offsetY;
    private String shape;
    private boolean auto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role")
    private Roles roles;

    //true for all, false for any
    private boolean all_any_role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "individual")
    private Individual particularIndividual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_associate")
    private Associates other_associate;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "predecessor")
    private Activity source;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "successor")
    private Activity destination;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private Workflow workflow;

    public Activity() {
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Activity getSource() {
        return source;
    }

    public void setSource(Activity source) {
        this.source = source;
    }

    public Activity getDestination() {
        return destination;
    }

    public void setDestination(Activity destination) {
        this.destination = destination;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public Individual getParticularIndividual() {
        return particularIndividual;
    }

    public void setParticularIndividual(Individual particularIndividual) {
        this.particularIndividual = particularIndividual;
    }

    public Associates getOther_associate() {
        return other_associate;
    }

    public void setOther_associate(Associates other_associate) {
        this.other_associate = other_associate;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isAll_any_role() {
        return all_any_role;
    }

    public void setAll_any_role(boolean all_any_role) {
        this.all_any_role = all_any_role;
    }
}
