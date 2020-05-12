package org.example.flowkit.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "activity_associates")
public class ActivityAssociates implements Serializable {
    private @Id @GeneratedValue Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_instance_associate")
    private ActivityInstance activity_instance_associate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "associate")
    private Associates associates;

    private String remark;

    // ACCEPT, REJECT, PENDING
    private String status;
    public ActivityAssociates() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActivityInstance getActivity_instance_associate() {
        return activity_instance_associate;
    }

    public void setActivity_instance_associate(ActivityInstance activity_instance_associate) {
        this.activity_instance_associate = activity_instance_associate;
    }

    public Associates getAssociates() {
        return associates;
    }

    public void setAssociates(Associates associates) {
        this.associates = associates;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
