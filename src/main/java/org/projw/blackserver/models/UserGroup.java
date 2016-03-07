package org.projw.blackserver.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 关于用户组有几个 id 是虚拟用户组，这种组内是没有具体用户的
 * 0 - 表示是用户的朋友圈形成的用户组
 * 1 - 表示是用户的关注对象形成的用户组
 * 2 - 表示使用户的粉丝形成的用户组
 * 正常的用户组从 GID_BASE 开始编号
 */
@SuppressWarnings("unused")
@Entity
@Table(name = "t_group")
public class UserGroup {
    public static final int GID_EMPTY = 0;
    public static final int GID_FRIENDS = 1;
    public static final int GID_FOLLOWINGS = 2;
    public static final int GID_FANS = 3;
    public static final int GID_BASE = 100;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String intro;

    @ManyToOne
    @JoinColumn(name = "logo_id")
    private Image logo;

    @OneToOne
    @JoinColumn(name = "page_id")
    private Page page;

    @Transient
    private String logoAccessToken;

    @OneToMany(mappedBy = "group")
    private Set<Membership> members = new HashSet<>();

    //<editor-fold desc="=== Getters & Setters ===">

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Long getPageId() {
        if (page != null) {
            return page.getId();
        }
        return null;
    }

    public String getLogoAccessToken() {
        return logoAccessToken;
    }

    public void setLogoAccessToken(String logoAccessToken) {
        this.logoAccessToken = logoAccessToken;
    }

    public Set<Membership> getMembers() {
        return members;
    }

    public void setMembers(Set<Membership> members) {
        this.members = members;
    }

    //</editor-fold>
}
