package org.team10424102.blackserver.game;

import com.fasterxml.jackson.annotation.JsonView;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.models.Image;

import javax.persistence.*;

@SuppressWarnings("unused")
@Entity
@Table(name = "t_game")
public class Game {
    @Id
    private Integer id;
    private String identifier;

    @ManyToOne
    @JoinColumn(name = "logo_id")
    private Image logo;

    @Transient
    private String localizedName;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonView(Views.Game.class)
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
