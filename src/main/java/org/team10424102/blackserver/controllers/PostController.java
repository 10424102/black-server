package org.team10424102.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.config.CurrentUser;
import org.team10424102.blackserver.models.LikesRepo;
import org.team10424102.blackserver.models.PostRepo;
import org.team10424102.blackserver.extensions.PostExtension;
import org.team10424102.blackserver.extensions.PostExtensionData;
import org.team10424102.blackserver.models.Post;
import org.team10424102.blackserver.models.PostLike;
import org.team10424102.blackserver.models.User;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(App.API_POST)
@Transactional
public class PostController {
    public static final String TYPE_SCHOOL = "school";
    public static final String TYPE_FRIENDS = "friends";
    public static final String TYPE_FOCUSES = "focuses";
    public static final String TYPE_MYSELF = "myself";

    private final Map<String, PostExtension> extensions = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        extensions.putAll(context.getBeansOfType(PostExtension.class));
    }

    private void inflateExtensionData(Post post) {
        String ext = post.getExtension();
        if (ext == null) return;
        String[] parts = ext.split(" ");
        PostExtension pe = extensions.get(parts[0]);
        if (pe != null) {
            Object data = pe.getData(parts[1]);
            post.setExtData(new PostExtensionData(parts[0], data));
        }
    }

    @Autowired PostRepo postRepo;

    @Autowired ApplicationContext context;

    @Autowired LikesRepo likesRepo;

    /**
     * 获取不同类别的 post, 目前以比赛战绩为主
     *
     * @param category school|friends|focuses|myself 对应于校园战况, 朋友战况, 关注的人战况, 自己发的推文
     */
    @RequestMapping(method = GET)
    @JsonView(Views.Post.class)
    public List<Post> getPosts(@RequestParam String category, Pageable pageable, @CurrentUser User user) {
        List<Post> posts = null;
        switch (category.toLowerCase()) {
            case TYPE_SCHOOL:
                posts = postRepo.findByCommentativeFalseAndSenderCollege(user.getCollege(), pageable);
                break;
            case TYPE_FRIENDS:
                posts = postRepo.findByCommentativeFalseAndSenderIn(user.getFriends(), pageable);
                break;
            case TYPE_FOCUSES:
                posts = postRepo.findByCommentativeFalseAndSenderIn(user.getFocuses(), pageable);
                break;
            case TYPE_MYSELF:
                posts = postRepo.findByCommentativeFalseAndSender(user, pageable);
                break;
        }
        if (posts != null) posts.forEach(this::inflateExtensionData);
        return posts;
    }

    /**
     * 创建一条推文
     */
    @RequestMapping(method = POST)
    @JsonView(Views.Post.class)
    public Post createPost(@RequestParam String content, @CurrentUser User user) {
        Post post = new Post();
        post.setCreationTime(new Date());
        post.setSender(user);
        post.setContent(content);
        return postRepo.save(post);
    }

    /**
     * 删除一条推文
     */
    @RequestMapping(value = "/{id}", method = DELETE)
    @JsonView(Views.Post.class)
    public void deletePost(@PathVariable long id, @CurrentUser User user) {
        Post post = postRepo.findOne(id);
        if (post != null && post.getSender().equals(user)) {
            postRepo.delete(post); // TODO check if deleted all related comments and likes
        }
    }

    @RequestMapping(value = "/{id}/likes", method = GET)
    public List<PostLike> getLikes(@PathVariable long id, Pageable pageable) {
        return postRepo.findLikesById(id, pageable);
    }

    /**
     * 点赞
     */
    @RequestMapping(value = "/{id}/likes", method = POST)
    public void likePost(@PathVariable long id, @CurrentUser User user) {
        Post post = postRepo.findOne(id);
        if (post == null) return;
        PostLike like = new PostLike();
        like.setCreationTime(new Date());
        like.setPost(post);
        like.setUser(user);
        likesRepo.save(like);
    }

    /**
     * 取消点赞
     */
    @RequestMapping(value = "/{id}/likes", method = DELETE)
    public void unlikePost(@PathVariable long id, @CurrentUser User user) {
        PostLike like = likesRepo.findOneByPostIdAndUser(id, user);
        if (like == null) return;
        likesRepo.delete(like);
    }

    /**
     * 评论
     */
    @RequestMapping(value = "/{id}/comments", method = POST)
    @JsonView(Views.Post.class)
    public Post CommentPost(@PathVariable(value = "id") Post post, @RequestParam String content, @CurrentUser User user) {
        Post comment = new Post(user, content, true);
        post.getComments().add(comment);
        postRepo.save(post);

        return comment;
    }

    /**
     * 得到所有评论
     */
    @RequestMapping(value = "/{id}/comments", method = GET)
    @JsonView(Views.PostComment.class)
    public Collection<Post> getComments(@PathVariable long id, Pageable pageable) {
        return postRepo.findCommentsById(id, pageable);
    }

}
