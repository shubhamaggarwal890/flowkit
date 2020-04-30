package org.example.flowkit.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Associates {
    private @Id @GeneratedValue Long id;
    @Column(nullable = false)

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String emailId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private byte[] salt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager")
    private Associates manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role")
    private Roles role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "workflowFor", cascade = CascadeType.ALL)
    private List<WorkflowInstance> workflowInstancesFor = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "associates", cascade = CascadeType.ALL)
    private List<ActivityAssociates> activityAssociates = new ArrayList<>();

    public Associates() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<WorkflowInstance> getWorkflowInstancesFor() {
        return workflowInstancesFor;
    }

    public void setWorkflowInstancesFor(List<WorkflowInstance> workflowInstancesFor) {
        this.workflowInstancesFor = workflowInstancesFor;
    }

    public List<ActivityAssociates> getActivityAssociates() {
        return activityAssociates;
    }

    public void setActivityAssociates(List<ActivityAssociates> activityAssociates) {
        this.activityAssociates = activityAssociates;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public Associates getManager() {
        return manager;
    }

    public void setManager(Associates manager) {
        this.manager = manager;
    }

}
