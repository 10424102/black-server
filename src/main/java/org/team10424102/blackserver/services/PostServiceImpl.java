package org.team10424102.blackserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team10424102.blackserver.models.PostLike;
import org.team10424102.blackserver.models.User;
import org.team10424102.blackserver.daos.LikesRepo;
import org.team10424102.blackserver.daos.PostRepo;
import org.team10424102.blackserver.models.Friendship;
import org.team10424102.blackserver.models.Post;
import org.team10424102.blackserver.extensions.PostExtension;
import org.team10424102.blackserver.extensions.PostExtensionData;
import org.team10424102.blackserver.extensions.PostExtensionIdentifier;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    public Map<String, PostExtension> extentions = new HashMap<>();

    @Autowired UserService userService;
    @Autowired PostRepo postRepo;
    @Autowired LikesRepo likesRepo;
    @Autowired ApplicationContext context;

    private String getExtentionIdentifier (Class cls) {
        PostExtensionIdentifier a = (PostExtensionIdentifier)cls.getAnnotation(PostExtensionIdentifier.class);
        String identifier = a.value();
        if (identifier == null) {
            identifier = cls.getCanonicalName();
        }
        return identifier;
    }

    @Override
    public void registerPostExtention(Class cls) {
        //TODO check if cls has implemented PostExtention
        PostExtension pe = (PostExtension)context.getBean(cls);
        extentions.put(getExtentionIdentifier(cls), pe);
    }

    @Override
    public void unregisterPostExtention(Class cls) {
        extentions.remove(getExtentionIdentifier(cls));
    }

    @Override
    public List<Post> getSchoolMatchPosts(Pageable pageable) {
        User user = userService.getCurrentUser();
        List<Post> posts = postRepo.findByCommentativeFalseAndSenderProfileCollege(user.getProfile().getCollege(), pageable);
        posts.forEach(this::inflatePostExtension);
        return posts;
    }

    private void inflatePostExtension(Post post) {
        String ext = post.getExtension();
        String[] parts = ext.split(" ");
        PostExtension pe = extentions.get(parts[0]);
        if (pe != null) {
            Object data = pe.getData(parts[1]);
            post.setExtData(new PostExtensionData(parts[0], data));
        }
    }

    @Override
    public List<Post> getFriendsMatchPosts(Pageable pageable) {
        User user = userService.getCurrentUser();
        Set<User> friends = user.getFriendshipSet().stream().map(Friendship::getFriend).collect(Collectors.toSet());
        List<Post> posts = postRepo.findByCommentativeFalseAndSenderIn(friends, pageable);
        posts.forEach(this::inflatePostExtension);
        return posts;
    }

    @Override
    public List<Post> getFocusesMatchPosts(Pageable pageable) {
        User user = userService.getCurrentUser();
        Set<User> focuses = user.getFocuses();
        List<Post> posts = postRepo.findByCommentativeFalseAndSenderIn(focuses, pageable);
        posts.forEach(this::inflatePostExtension);
        return posts;
    }

    @Override
    public List<Post> getMyMatchPosts(Pageable pageable) {
        User user = userService.getCurrentUser();
        List<Post> posts = postRepo.findByCommentativeFalseAndSender(user, pageable);
        posts.forEach(this::inflatePostExtension);
        return posts;
    }

    @Override
    public List<Post> getCommentPosts(Pageable pageable, long postId) {
        List<Post> posts = postRepo.findCommentsById(postId, pageable);
        posts.forEach(this::inflatePostExtension);
        return posts;
    }

    @Override
    public void likePost(long postId) {
        User user = userService.getCurrentUser();
        PostLike like = new PostLike();
        like.setCreationTime(new Date());
        like.setPost(postRepo.findOne(postId));
        like.setUser(user);
        likesRepo.save(like);
    }

    @Override
    public void unlikePost(long postId) {
        User user = userService.getCurrentUser();
        PostLike like = likesRepo.findOneByPostIdAndUser(postId, user);
        likesRepo.delete(like);
    }

    @Override
    @Transactional
    public void commentPost(long postId, String content) {
        User user = userService.getCurrentUser();
        Post comment = new Post();
        comment.setCreationTime(new Date());
        comment.setSender(user);
        comment.setContent(content);

        postRepo.save(comment);

        Post post = postRepo.findOne(postId);
        post.getComments().add(comment);

        postRepo.save(post);
    }

    @Override
    public void createPost(String content) {
        User user = userService.getCurrentUser();
        Post post = new Post();
        post.setCreationTime(new Date());
        post.setSender(user);
        post.setContent(content);

        postRepo.save(post);
    }

    @Override
    public void deletePost(long postId) {
        User user = userService.getCurrentUser();
        Post post = postRepo.findOne(postId);
        if (post.getSender().equals(user)) {
            postRepo.delete(post);
        }
    }

    @Override
    public List<PostLike> getLikes(long postId, Pageable pageable) {
        List<PostLike> likes = postRepo.findLikesById(postId, pageable);
        return likes;
    }
}
