package org.team10424102.blackserver.models;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.transaction.annotation.Transactional;
import org.team10424102.blackserver.config.json.UserDeserializer;
import org.team10424102.blackserver.config.json.Views;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 正常的用户从 UID_BASE 开始编号
 */
@Entity
@JsonDeserialize(using = UserDeserializer.class)
@Table(name = "t_user")
public class User {
    public static final int UID_SYSTEM = 0;
    public static final int UID_BASE = 100;

    /**
     * 编号, 普通用户从 100 开始计数
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 用户名, 使用户在服务器内的唯一标识符, 由英文字母组成
     */
    @NotBlank
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$")
    @Column(unique = true)
    private String username;

    /**
     * 电子邮箱, 需要通过验证, 才能激活
     */
    @Basic(fetch = FetchType.LAZY)
    @Pattern(regexp = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$")
    private String email;

    /**
     * 是否被激活
     */
    private Boolean enabled = true;

    /**
     * 角色
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "t_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles = new HashSet<>();

    /**
     * 注册信息
     */
    @Embedded
    private RegInfo regInfo = new RegInfo();

    /**
     * 关注对象
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "t_subscription",
            joinColumns = @JoinColumn(name = "subscriber_id"),
            inverseJoinColumns = @JoinColumn(name = "broadcaster_id")
    )
    private Set<User> focuses = new HashSet<>();

    /**
     * 粉丝
     */
    @ManyToMany(mappedBy = "focuses", fetch = FetchType.LAZY)
    private Set<User> fans = new HashSet<>();

    /**
     * 好友
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Friendship> friendshipSet = new HashSet<>();

    /**
     * 参加的群组
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<Membership> membershipSet = new HashSet<>();

    /**
     * 昵称, 可以重复, 例如张三
     */
    @Length(max = 30, min = 2)
    private String nickname;

    /**
     * 用户的真实姓名
     */
    @Length(max = 30, min = 2)
    private String realname;

    /**
     * 中华人民共和国居民身份证号码, 15 位或者 18 位
     */
    @Pattern(regexp = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)")
    private String idcard;

    /**
     * 性别: MALE, FEMALE, OTHER, UNKNOWN
     */
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private Gender gender;

    /**
     * 头像
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Image avatar;

    /**
     * 背景图片
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "background_id")
    private Image background;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 个性化签名
     */
    private String signature;

    /**
     * 家乡
     */
    private String hometown;

    /**
     * 中国大陆 11 位手机号码 (+86)
     */
    @Pattern(regexp = "((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)")
    private String phone;

    /**
     * 高中
     */
    @Length(min = 2, max = 30)
    private String highschool;

    /**
     * 大学
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    private College college;

    /**
     * 学院
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academy_id")
    private Academy academy;

    /**
     * 大学所在的年级
     */
    private String grade;

    /**
     * 黑名单
     */
    @ManyToMany
    @JoinTable(
            name = "t_user_blacklist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "target_id")
    )
    private Set<User> blacklist = new HashSet<>();

    @Transient
    private String alias;

    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                        GET & SET                            //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Getters & Setters ===">

    @JsonView(Views.UserSummary.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    @JsonView(Views.UserSummary.class)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @JsonIgnore
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @JsonIgnore
    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
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
    public Image getBackground() {
        return background;
    }

    public void setBackground(Image backgroundImage) {
        this.background = backgroundImage;
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

    @JsonView(Views.UserDetails.class)
    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    @JsonView(Views.UserDetails.class)
    public Academy getAcademy() {
        return academy;
    }

    public void setAcademy(Academy academy) {
        this.academy = academy;
    }

    @JsonView(Views.UserDetails.class)
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }


//    @JsonProperty("college")
//    @JsonView(Views.UserDetails.class)
//    public String getCollegeName() {
//        if (college != null) {
//            return college.getName();
//        }
//        return null;
//    }

//    @JsonProperty("academy")
//    @JsonView(Views.UserDetails.class)
//    public String getAcademyName() {
//        if (academy != null) {
//            return academy.getName();
//        }
//        return null;
//    }

    @JsonIgnore
    public Set<User> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(Set<User> blacklist) {
        this.blacklist = blacklist;
    }

//    @JsonProperty("friends")
//    @JsonView(Views.UserDetails.class)
//    public long getFriendsCount() {
//        if (friendshipSet != null) {
//            return friendshipSet.size();
//        }
//        return 0;
//    }

//    @JsonProperty("fans")
//    @JsonView(Views.UserDetails.class)
//    public long getFanssCount() {
//        if (fans != null) {
//            return fans.size();
//        }
//        return 0;
//    }

//    @JsonProperty("focuses")
//    @JsonView(Views.UserDetails.class)
//    public long getFocusesCount() {
//        if (focuses != null) {
//            return focuses.size();
//        }
//        return 0;
//    }

    @JsonIgnore
    public List<User> getFriends() {
        return getFriendshipSet().stream().map(e -> {
            User friend = e.getFriend();
            friend.setAlias(e.getFriendAlias());
            return friend;
        }).collect(Collectors.toList());
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    //</editor-fold>

}
