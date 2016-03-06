package org.projw.blackserver.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.projw.blackserver.config.json.Views;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_friendship_application")
public class FriendshipApplication {
    @Id
    @GeneratedValue
    private Long id;

    //@ManyToOne(cascade = CascadeType.PERSIST)
    @ManyToOne
    @JoinColumn(name = "applicant_id")
    private User applicant;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private User target;

    private String attachment;

    private Date creationTime = new Date();

    @OneToOne
    @JoinColumn(name = "applicant_notification_id")
    private Notification applicantNotification;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.FriendshipApplication.class)
    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    @JsonView(Views.FriendshipApplication.class)
    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    @JsonView(Views.FriendshipApplication.class)
    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @JsonView(Views.FriendshipApplication.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @JsonIgnore
    public Notification getApplicantNotification() {
        return applicantNotification;
    }

    public void setApplicantNotification(Notification applicantNotification) {
        this.applicantNotification = applicantNotification;
    }
}
