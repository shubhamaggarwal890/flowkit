package org.example.flowkit.jsonobject;

public class ActivityAssociateRequest {
    private Long workflow;
    private Long activity_instance;
    private Long associate;
    private String status;
    private Boolean any_all;
    private String remark;

    public Long getActivity_instance() {
        return activity_instance;
    }

    public void setActivity_instance(Long activity_instance) {
        this.activity_instance = activity_instance;
    }

    public Long getAssociate() {
        return associate;
    }

    public void setAssociate(Long associate) {
        this.associate = associate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getAny_all() {
        return any_all;
    }

    public void setAny_all(Boolean any_all) {
        this.any_all = any_all;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Long workflow) {
        this.workflow = workflow;
    }
}
