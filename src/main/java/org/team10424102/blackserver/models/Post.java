package org.team10424102.blackserver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.extensions.PostExtensionData;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
@Entity
@Table(name = "t_post")
public class Post {
    public static final String TAG_MATCH = "战况";

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    private Date creationTime;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    private String device;

    private String extension;

    @OneToMany(mappedBy = "post")
    private Set<PostLike> likes = new HashSet<>();

    @OneToMany
    @JoinTable(
            name = "T_POST_COMMENTS",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "reply_id")
    )
    private Set<Post> comments = new HashSet<>();

    //@Column(columnDefinition = "tinyint")
    private boolean commentative;

    @Transient
    private PostExtensionData extData;

    /////////////////////////////////////////////////////////////////
    //                                                             //
    //                    ~~~~~~~~~~~~~~~~~                        //
    //                        GET & SET                            //
    //                    =================                        //
    //                                                             //
    /////////////////////////////////////////////////////////////////

    //<editor-fold desc="=== Getters & Setters ===">

    @JsonView(Views.Post.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Post.class)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonView(Views.Post.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @JsonView(Views.Post.class)
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    @JsonView(Views.Post.class)
    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    @JsonIgnore
    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @JsonIgnore
    public Set<PostLike> getLikes() {
        return likes;
    }

    public void setLikes(Set<PostLike> likes) {
        this.likes = likes;
    }

    @JsonIgnore
    public Set<Post> getComments() {
        return comments;
    }

    public void setComments(Set<Post> comments) {
        this.comments = comments;
    }

    public boolean isCommentative() {
        return commentative;
    }

    public void setCommentative(boolean commentative) {
        this.commentative = commentative;
    }

    //</editor-fold>

    @JsonProperty("likes")
    @JsonView(Views.Post.class)
    public int getLikesCount() {
        if (likes!=null) return likes.size();
        return 0;
    }

    @JsonProperty("comments")
    @JsonView(Views.Post.class)
    public int getCommentsCount() {
        if (comments!=null) return comments.size();
        return 0;
    }

    @JsonProperty("extension")
    @JsonView(Views.Post.class)
    public PostExtensionData getExtData() {
        return extData;
    }

    public void setExtData(PostExtensionData extData) {
        this.extData = extData;
    }
}
