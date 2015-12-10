package org.team10424102.blackserver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.team10424102.blackserver.config.json.UserDeserializer;
import org.team10424102.blackserver.config.json.Views;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 正常的用户从 UID_BASE 开始编号
 */
@SuppressWarnings("unused")
@Entity
@JsonDeserialize(using = UserDeserializer.class)
public class User {
    public static final int UID_SYSTEM = 0;
    public static final int UID_BASE = 100;

    @Id
    @GeneratedValue
    private Integer id;

    private String username;

    @Basic(fetch = FetchType.LAZY)
    private String email;

    //@Column(columnDefinition = "tinyint")
    private Boolean enabled;

    @Embedded
    private Profile profile = new Profile();

    @Embedded
    private RegInfo regInfo = new RegInfo();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "T_SUBSCRIPTION",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "broadcaster_id")
    )
    private Set<User> focuses = new HashSet<>();

    @ManyToMany(mappedBy = "focuses", fetch = FetchType.LAZY)
    private Set<User> fans = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Friendship> friendshipSet = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Membership> membershipSet = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable (
            name = "T_AUTHORITIES",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles = new HashSet<>();




    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                        GET & SET                            //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Getters & Setters ===">

    @JsonIgnore
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonView(Views.UserSummary.class)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(Views.UserDetails.class)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @JsonView(Views.UserSummary.class)
    @JsonUnwrapped
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @JsonIgnore
    public RegInfo getRegInfo() {
        return regInfo;
    }

    public void setRegInfo(RegInfo regInfo) {
        this.regInfo = regInfo;
    }

    @JsonIgnore
    public Set<User> getFocuses() {
        return focuses;
    }

    public void setFocuses(Set<User> focuses) {
        this.focuses = focuses;
    }

    @JsonIgnore
    public Set<User> getFans() {
        return fans;
    }

    public void setFans(Set<User> fans) {
        this.fans = fans;
    }

    @JsonIgnore
    public Set<Friendship> getFriendshipSet() {
        return friendshipSet;
    }

    public void setFriendshipSet(Set<Friendship> friendshipSet) {
        this.friendshipSet = friendshipSet;
    }

    @JsonIgnore
    public Set<Membership> getMembershipSet() {
        return membershipSet;
    }

    public void setMembershipSet(Set<Membership> membershipSet) {
        this.membershipSet = membershipSet;
    }

    @JsonIgnore
    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    //</editor-fold>

}
