package com.example.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonDeserialize(using = com.example.config.converters.json.UserDeserilizer.class)
public class User {

    @Id
    @GeneratedValue
    private Integer id;

    private String phone;

    private String email;

    private boolean enabled = true;

    @Embedded
    private Profile profile = new Profile();

    @Embedded
    private RegistrationInfo regInfo = new RegistrationInfo();

    @ManyToMany
    @JoinTable(
            name = "T_SUBSCRIPTION",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "broadcaster_id")
    )
    private Set<User> focuses = new HashSet<>();

    @ManyToMany(mappedBy = "focuses")
    private Set<User> fans = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Friendship> friendshipSet = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Membership> membershipSet = new HashSet<>();

    public User() {
    }


    public User(String phone) {
        this.phone = phone;
    }

    //<editor-fold desc="=== Getters & Setters ===">

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<User> getFocuses() {
        return focuses;
    }

    public void setFocuses(Set<User> focuses) {
        this.focuses = focuses;
    }

    public RegistrationInfo getRegInfo() {
        return regInfo;
    }

    public void setRegInfo(RegistrationInfo regInfo) {
        this.regInfo = regInfo;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Set<User> getFans() {
        return fans;
    }

    public void setFans(Set<User> fans) {
        this.fans = fans;
    }

    public Set<Friendship> getFriendshipSet() {
        return friendshipSet;
    }

    public void setFriendshipSet(Set<Friendship> friendshipSet) {
        this.friendshipSet = friendshipSet;
    }

    public Set<Membership> getMembershipSet() {
        return membershipSet;
    }

    public void setMembershipSet(Set<Membership> membershipSet) {
        this.membershipSet = membershipSet;
    }

    //</editor-fold>

}
