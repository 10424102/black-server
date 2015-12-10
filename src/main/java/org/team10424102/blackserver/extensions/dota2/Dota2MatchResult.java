package org.team10424102.blackserver.extensions.dota2;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.game.MatchResult;
import org.team10424102.blackserver.models.User;

import javax.persistence.*;

@Entity
@Table(name = "T_DOTA2_MATCH_RESULT")
@SuppressWarnings("unused")
public class Dota2MatchResult {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hero_id")
    private Dota2Hero hero;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private User player;

    private String evaluation;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum")
    private Dota2MatchType type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "enum('LADDER')")
    private MatchResult result;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Dota2Match match;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Post.class)
    @JsonUnwrapped
    public Dota2Hero getHero() {
        return hero;
    }

    public void setHero(Dota2Hero hero) {
        this.hero = hero;
    }

    @JsonIgnore
    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    @JsonView(Views.Post.class)
    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    @JsonView(Views.Post.class)
    public Dota2MatchType getType() {
        return type;
    }

    public void setType(Dota2MatchType type) {
        this.type = type;
    }

    @JsonView(Views.Post.class)
    public MatchResult getResult() {
        return result;
    }

    public void setResult(MatchResult result) {
        this.result = result;
    }

    @JsonIgnore
    public Dota2Match getMatch() {
        return match;
    }

    public void setMatch(Dota2Match match) {
        this.match = match;
    }

//    @JsonProperty("hero")
//    @JsonView(Views.Post.class)
//    public String getHeroName(){
//        return hero.getName();
//    }
}
