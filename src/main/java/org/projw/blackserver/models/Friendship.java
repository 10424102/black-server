package org.projw.blackserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import org.projw.blackserver.config.json.Views;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "t_friendship")
public class Friendship {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    private String friendAlias;

    //<editor-fold desc="=== Getters & Setters ===">

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonUnwrapped
    @JsonView(Views.UserSummary.class)
    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    @JsonProperty("alias")
    @JsonView(Views.UserSummary.class)
    public String getFriendAlias() {
        return friendAlias;
    }

    public void setFriendAlias(String friendAlias) {
        this.friendAlias = friendAlias;
    }

    //</editor-fold>
}
