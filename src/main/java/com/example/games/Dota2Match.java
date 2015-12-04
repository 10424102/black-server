package com.example.games;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Dota2Match {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "match")
    private Set<Dota2MatchResult> results = new HashSet<>();

    private Date startTime;

    private Date endTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Dota2MatchResult> getResults() {
        return results;
    }

    public void setResults(Set<Dota2MatchResult> results) {
        this.results = results;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
