package org.team10424102.blackserver.extensions.poll;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_poll")
public class Poll {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @OneToMany(mappedBy = "poll")
    private Set<Vote> votes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<Vote> getVotes() {
        return votes;
    }

    public void setVotes(Set<Vote> votes) {
        this.votes = votes;
    }
}
