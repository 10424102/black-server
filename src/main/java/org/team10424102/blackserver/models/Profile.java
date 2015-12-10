package org.team10424102.blackserver.models;

import com.fasterxml.jackson.annotation.*;
import org.team10424102.blackserver.config.json.Views;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings("unused")
@Embeddable
public class Profile {

    private String nickname;

    @Column(name = "realname")
    private String realName;

    @Column(name = "idcard")
    private String idCard;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Image avatar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "background_image_id")
    private Image backgroundImage;

    private Date birthday;

    private String signature;

    private String hometown;

    private String phone;

    private String highschool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academy_id")
    private Academy academy;

    private String grade;


    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                        GET & SET                            //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Getters & Setters ===">

    @JsonView(Views.UserSummary.class)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @JsonIgnore
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @JsonIgnore
    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    @JsonView(Views.UserDetails.class)
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @JsonView(Views.UserSummary.class)
    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    @JsonView(Views.UserDetails.class)
    @JsonProperty("background")
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    @JsonView(Views.UserDetails.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @JsonView(Views.UserSummary.class)
    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @JsonView(Views.UserDetails.class)
    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    @JsonView(Views.UserDetails.class)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonView(Views.UserDetails.class)
    public String getHighschool() {
        return highschool;
    }

    public void setHighschool(String highschool) {
        this.highschool = highschool;
    }

    @JsonUnwrapped
    @JsonView(Views.UserDetails.class)
    @JsonIgnore
    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    @JsonIgnore
    public Academy getAcademy() {
        return academy;
    }

    public void setAcademy(Academy academy) {
        this.academy = academy;
    }

    @JsonIgnore
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    //</editor-fold>

    @JsonProperty("college")
    @JsonView(Views.UserDetails.class)
    public String getCollegeName() {
        if (college != null) {
            return college.getName();
        }
        return null;
    }

    @JsonProperty("academy")
    @JsonView(Views.UserDetails.class)
    public String getAcademyName() {
        if (academy != null) {
            return academy.getName();
        }
        return null;
    }
}
