package org.example.flowkit.entity;

import javax.persistence.*;

@Entity
public class Document {
    private @Id @GeneratedValue Long id;

    private String title;

    @Column(nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader", nullable = false)
    private Associates uploader;

    @OneToOne(mappedBy = "document", fetch = FetchType.LAZY)
    private ActivityInstance activityInstance;

    public Document() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Associates getUploader() {
        return uploader;
    }

    public void setUploader(Associates uploader) {
        this.uploader = uploader;
    }

    public ActivityInstance getActivityInstance() {
        return activityInstance;
    }

    public void setActivityInstance(ActivityInstance activityInstance) {
        this.activityInstance = activityInstance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
