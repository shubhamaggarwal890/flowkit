package org.example.flowkit.jsonobject;

public class AssociateResponse {
    private Long id;
    private String firstName;
    RoleRequest role;

    public AssociateResponse() {
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

    public RoleRequest getRole() {
        return role;
    }

    public void setRole(RoleRequest role) {
        this.role = role;
    }
}
