package org.projw.blackserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.projw.blackserver.config.json.Views;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_notification")
public class Notification {
    public static final int SYSTEM = 1;
    public static final int FRIEND_ADD = 100;
    public static final int FRIEND_ADD_PENDING = 101;
    public static final int FRIEND_ADD_REPLY = 102;
    public static final int FRIEND_REMOVE = 150;
    public static final int GROUP_JOIN = 200;
    public static final int GROUP_JOIN_PENDING = 201;
    public static final int GROUP_JOIN_REPLY = 202;
    public static final int GROUP_QUIT = 250;
    public static final int ACTIVITY_JOIN = 300;
    public static final int ACTIVITY_JOIN_PENDING = 301;
    public static final int ACTIVITY_JOIN_REPLY = 302;
    public static final int ACTIVITY_QUIT = 350;

    public static final int REPLY_YES = 1;
    public static final int REPLY_NO = 2;
    public static final int REPLY_DELETE = 3;

    @Id
    @GeneratedValue
    private Long id;

    private int type;

    private Long dataId;

    @ManyToOne
    @JoinColumn(name = "target_id")
    private User target;

    private Date expiration;

    private int reply;

    @Transient
    private Object data;

    @JsonView(Views.Notification.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Notification.class)
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JsonView(Views.Notification.class)
    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    @JsonIgnore
    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @JsonIgnore
    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    @JsonView(Views.Notification.class)
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @JsonView(Views.Notification.class)
    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }
}
