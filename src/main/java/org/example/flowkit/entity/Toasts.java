package org.example.flowkit.entity;

import javax.persistence.*;

@Entity
public class Toasts {
    private @Id
    @GeneratedValue
    Long id;
    String message;
    boolean notified = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notify")
    private Associates notify;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_instance")
    private ActivityInstance activityInstance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Associates getNotify() {
        return notify;
    }

    public void setNotify(Associates notify) {
        this.notify = notify;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public ActivityInstance getActivityInstance() {
        return activityInstance;
    }

    public void setActivityInstance(ActivityInstance activityInstance) {
        this.activityInstance = activityInstance;
    }
}
