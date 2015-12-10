package org.team10424102.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.models.PostLike;
import org.team10424102.blackserver.services.PostService;
import org.team10424102.blackserver.models.Post;

import java.util.List;
import java.util.Locale;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class PostController {
    public static final String TYPE_SCHOOL = "school";
    public static final String TYPE_FRIENDS = "friends";
    public static final String TYPE_FOCUSES = "focuses";
    public static final String TYPE_MYSELF = "myself";

    @Autowired PostService postService;

    /**
     * 获取不同类别的 post, 目前以比赛战绩为主
     *
     * @param category school|friends|focuses|myself 对应于校园战况, 朋友战况, 关注的人战况, 自己发的推文
     */
    @RequestMapping(value = App.API_POST + "/{category}", method = GET)
    @JsonView(Views.Post.class)
    public List<Post> getPosts(@PathVariable String category, Pageable pageable) {
        List<Post> posts = null;
        switch (category.toLowerCase()) {
            case TYPE_SCHOOL:
                posts = postService.getSchoolMatchPosts(pageable);
                break;
            case TYPE_FRIENDS:
                posts = postService.getFriendsMatchPosts(pageable);
                break;
            case TYPE_FOCUSES:
                posts = postService.getFocusesMatchPosts(pageable);
                break;
            case TYPE_MYSELF:
                posts = postService.getMyMatchPosts(pageable);
                break;
        }
        return posts;
    }

    @RequestMapping(value = App.API_POST, method = POST)
    public void createPost(@RequestParam String content) {
        postService.createPost(content);
    }

    @RequestMapping(value = App.API_POST + "/{id}", method = DELETE)
    public void deletePost(@PathVariable long id) {
        postService.deletePost(id);
    }

    @RequestMapping(value = App.API_POST + "/{id}/like", method = POST)
    public void likePost(@PathVariable long id) {
        Post post = new Post();
        post.setId(id);
        postService.likePost(id);
    }

    @RequestMapping(value = App.API_POST + "/{id}/like", method = GET)
    public List<PostLike> getLikes(@PathVariable long id, Pageable pageable) {
        Post post = new Post();
        post.setId(id);
        return postService.getLikes(id, pageable);
    }

    @RequestMapping(value = App.API_POST + "/{id}/comment", method = POST)
    public void commentPost(@PathVariable long id, @RequestParam String content) {
        Post post = new Post();
        post.setId(id);
        postService.commentPost(id, content);
    }

    @RequestMapping(value = App.API_POST + "/{id}/comment", method = GET)
    public List<Post> getComments(@PathVariable long id, QueryCriteria criteria) {
        Post post = new Post();
        post.setId(id);
        return postService.getCommentPosts(criteria.toPageRequest(), id);
    }

}
