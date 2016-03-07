package org.projw.blackserver.models;

import com.fasterxml.jackson.annotation.JsonView;
import org.projw.blackserver.config.json.Views;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "t_game")
public class Game {
    @Id
    private Long id;

    private String nameKey;

    @ManyToOne
    @JoinColumn(name = "logo_id")
    private Image logo;

    @Transient
    private String localizedName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Game.class)
    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    @JsonView(Views.Game.class)
    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    @JsonView(Views.Game.class)
    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }
}
