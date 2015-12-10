package org.team10424102.blackserver.models;

import javax.persistence.*;

@Entity
@Table(name = "t_activity_recommendation")
public class ActivityRecommendation {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "int")
    private Long id;

    @OneToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
