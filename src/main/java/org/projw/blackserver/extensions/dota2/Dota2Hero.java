package org.projw.blackserver.extensions.dota2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.projw.blackserver.config.json.Views;
import org.projw.blackserver.models.Image;

import javax.persistence.*;

@Entity
@Table(name = "t_dota2_hero")
public class Dota2Hero {
    @Id
    @GeneratedValue
    private Long id;
    private String identifier;

    @OneToOne
    @JoinColumn(name = "avatar_id")
    private Image avatar;

    @Transient
    private String heroName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("heroAvatar")
    @JsonView(Views.Post.class)
    public Image getAvatar() {
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    @JsonView(Views.Post.class)
    @JsonProperty("hero")
    public String getHeroName() {
        return heroName;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }
}
