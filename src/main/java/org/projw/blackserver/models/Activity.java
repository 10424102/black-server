package org.projw.blackserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.projw.blackserver.config.json.ActivityDeserializer;
import org.projw.blackserver.config.json.Views;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
@JsonDeserialize(using = ActivityDeserializer.class)
@Table(name = "t_activity")
public class Activity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cover_image_id")
    private Image coverImage;

    private Date startTime;

    private Date endTime;

    private Date registrationDeadline;

    private Date creationTime;

    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promoter_id")
    private User promoter;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    private String tags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private UserGroup group;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_activity_image",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private Set<Image> photos = new HashSet<>();


    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_activity_comments",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private Set<Post> comments = new HashSet<>();


    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                        GET & SET                            //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Getters & Setters ===">



    @JsonView(Views.ActivitySummary.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("cover")
    @JsonView(Views.ActivitySummary.class)
    public Image getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Image coverImage) {
        this.coverImage = coverImage;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonView(Views.ActivitySummary.class)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonView(Views.ActivitySummary.class)
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    @JsonView(Views.ActivitySummary.class)
    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    @JsonView(Views.ActivitySummary.class)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @JsonView(Views.ActivityDetails.class)
    public User getPromoter() {
        return promoter;
    }

    public void setPromoter(User promoter) {
        this.promoter = promoter;
    }

    @JsonView(Views.ActivityDetails.class)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @JsonView(Views.ActivitySummary.class)
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @JsonView(Views.ActivitySummary.class)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonIgnore
    public UserGroup getGroup() {
        return group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    @JsonView(Views.ActivityDetails.class)
    public Set<Image> getPhotos() {
        return photos;
    }

    public void setPhotos(Set<Image> photos) {
        this.photos = photos;
    }

    @JsonView(Views.ActivityDetails.class)
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Set<Post> getComments() {
        return comments;
    }

    public void setComments(Set<Post> comments) {
        this.comments = comments;
    }

    //</editor-fold>

    @JsonProperty("game")
    @JsonView(Views.ActivitySummary.class)
    public String getGameName() {
        if (game != null) {
            return game.getNameKey();
        }
        return null;
    }

    @JsonProperty("group")
    @JsonView(Views.ActivityDetails.class)
    public Long getGroupId() {
        if (group != null) {
            return group.getId();
        }
        return null;
    }

    @JsonProperty("comments")
    @JsonView(Views.ActivityDetails.class)
    public int getCommentsCount() {
        if (comments!=null) return comments.size();
        return 0;
    }
}
