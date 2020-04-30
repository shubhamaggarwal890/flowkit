package org.example.flowkit.jsonobject;

public class IndividualRequest {
    private String id;
    private String name;
    private AssociateRequest individual;

    public IndividualRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssociateRequest getIndividual() {
        return individual;
    }

    public void setIndividual(AssociateRequest individual) {
        this.individual = individual;
    }
}
