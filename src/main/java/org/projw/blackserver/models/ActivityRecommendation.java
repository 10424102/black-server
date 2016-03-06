package org.projw.blackserver.models;

import javax.persistence.*;

@Entity
@Table(name = "t_activity_recommendation")
public class ActivityRecommendation {
    @Id
    @GeneratedValue
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
