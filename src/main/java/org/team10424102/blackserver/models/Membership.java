package org.team10424102.blackserver.models;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "t_membership")
public class Membership {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private UserGroup group;

    @Enumerated
    @Column(columnDefinition = "ENUM('MEMBER', 'OP', 'SPEAKER')")
    private MemberType type;

    private String nickname;

    private String groupAlias;

    //<editor-fold desc="=== Getters & Setters ===">

    public MemberType getType() {
        return type;
    }

    public void setType(MemberType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupAlias() {
        return groupAlias;
    }

    public void setGroupAlias(String groupAlias) {
        this.groupAlias = groupAlias;
    }

    //</editor-fold>
}
