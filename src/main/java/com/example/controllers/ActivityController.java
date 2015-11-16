package com.example.controllers;

import com.example.config.jsonviews.ActivityView;
import com.example.daos.ActivityRepo;
import com.example.daos.GameRepo;
import com.example.daos.GroupRepo;
import com.example.daos.UserRepo;
import com.example.models.*;
import com.example.services.ImageService;
import com.example.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@RestController
public class ActivityController {

    public final static String OWNER = "owner";
    public final static String MEMBER = "member";

    @Autowired UserService userService;

    @Autowired UserRepo userRepo;

    @Autowired GameRepo gameRepo;

    @Autowired GroupRepo groupRepo;

    @Autowired ActivityRepo activityRepo;

    @Autowired ImageService imageService;

    /**
     * 获取全部活动
     *
     * @param page 分页的起始号
     * @param size 每个分页包含的条目数
     * @return 活动的概要信息
     */
    @RequestMapping(value = "/api/activities", method = RequestMethod.GET)
    @JsonView(ActivityView.ActivitySummary.class)
    public ResponseEntity<?> getRecentActivities(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "fliter", required = false) String filter,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.DESC, "startTime");
        Page<Activity> activities = activityRepo.findAll(pageRequest);
        if (activities.getTotalElements() <= 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Activity> activityList = activities.getContent();
        Activity activity = null;
        Image coverImage = null;
        for (Iterator<Activity> iterator = activityList.iterator(); iterator.hasNext(); ) {
            activity = iterator.next();
            coverImage = activity.getCoverImage();
            if (coverImage != null) {
                activity.setCoverImageAccessToken(imageService.generateAccessToken(coverImage));
            }
        }
        return new ResponseEntity<>(activityList, HttpStatus.OK);
    }

    /**
     * 获取活动的详细信息
     *
     * @param id 活动的Id
     * @return 活动的详细信息
     */
    @RequestMapping(value = "/api/activities/{id}", method = RequestMethod.GET)
    @JsonView(ActivityView.ActivityDetails.class)
    public ResponseEntity<?> createActivity(@PathVariable(value = "id") Integer id) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Activity activity = activityRepo.findOne(id);
        if (activity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Image coverImage = activity.getCoverImage();
        if (coverImage != null) {
            activity.setCoverImageAccessToken(imageService.generateAccessToken(coverImage));
        }
        Set<Image> photos = activity.getPhotos();
        Image photo = null;
        if (!photos.isEmpty()) {
            Set<String> accessTokens = new HashSet<>();
            for (Iterator<Image> iterator = photos.iterator(); iterator.hasNext(); ) {
                photo = iterator.next();
                if (photo != null) {
                    accessTokens.add(imageService.generateAccessToken(photo));
                }
            }
            activity.setPhotosAccessToken(accessTokens);
        }
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    /**
     * @param activity
     * @return
     */
    @RequestMapping(value = "/api/activities", method = RequestMethod.POST)
    @JsonView(ActivityView.ActivityDetails.class)
    public ResponseEntity<?> createActivity(@RequestBody Activity activity) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        activity.setPromoter(currentUser);
        Game game = activity.getGame();
        Game targetGame = gameRepo.findOneByName(game.getName());
        if (targetGame == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        activity.setGame(targetGame);
        UserGroup group = activity.getGroup();
        UserGroup targetGroup = groupRepo.findOne(group.getId());
        if (targetGroup == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        activity.setGroup(targetGroup);
        activityRepo.save(activity);
        return new ResponseEntity<>(activity, HttpStatus.CREATED);
    }

    /**
     * @param hh
     * @return
     */
    @RequestMapping(value = "/api/activities/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<?> updateParticleActivity(@RequestParam(value = "hh", required = false) String hh) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * 获取当前用户所参与的活动的概述信息
     *
     * @param type 用户参与类型：OWNER表示用户举办的，MEMBER表示全部用户参与的
     * @param page 分页的起始号
     * @param size 每个分页包含的条目数
     * @return 当前用户所参与活动的概述信息
     */
    @RequestMapping(value = "/api/user/activities", method = RequestMethod.GET)
    @JsonView(ActivityView.ActivitySummary.class)
    public ResponseEntity<?> getUserActivity(
            @RequestParam(value = "type", defaultValue = OWNER) String type,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.DESC, "startTime");
        Page<Activity> activitiesPage = null;
        List<Activity> activities = null;
        Activity activity = null;
        Image coverImage = null;
        switch (type) {
            case OWNER:
                activitiesPage = activityRepo.findByPromoter(currentUser, pageRequest);
                if (activitiesPage.getTotalElements() < 1) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                activities = activitiesPage.getContent();
                for (Iterator<Activity> iterator = activities.iterator(); iterator.hasNext(); ) {
                    activity = iterator.next();
                    coverImage = activity.getCoverImage();
                    if (coverImage != null) {
                        activity.setCoverImageAccessToken(imageService.generateAccessToken(coverImage));
                    }
                }
                return new ResponseEntity<>(activities, HttpStatus.OK);
            case MEMBER:
                activitiesPage = activityRepo.findByPromoter(currentUser, pageRequest);
                if (activitiesPage.getTotalElements() < 1) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                activities = activitiesPage.getContent();
                for (Iterator<Activity> iterator = activities.iterator(); iterator.hasNext(); ) {
                    activity = iterator.next();
                    coverImage = activity.getCoverImage();
                    if (coverImage != null) {
                        activity.setCoverImageAccessToken(imageService.generateAccessToken(coverImage));
                    }
                }
                return new ResponseEntity<>(activities, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 获取指定用户所参与的活动的概述信息
     *
     * @param id   指定用户的id
     * @param type 参与类型：OWENER表示该用户举办的，MEMBER表示该用户参与的全部活动
     * @param page 分页的起始号
     * @param size 每个分页包含的条目数
     * @return 指定用户所参与的活动的概述信息
     */
    @RequestMapping(value = "/api/users/{Id}/activities", method = RequestMethod.GET)
    @JsonView(ActivityView.ActivitySummary.class)
    public ResponseEntity<?> getUserActivity(
            @PathVariable(value = "Id") Integer id,
            @RequestParam(value = "type", defaultValue = OWNER) String type,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "5") Integer size) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User targetUser = userRepo.findOne(id);
        if (targetUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Page<Activity> activitiesPage = null;
        List<Activity> activities = null;
        Activity activity = null;
        Image coverImage = null;
        final PageRequest pageRequest = new PageRequest(page, size, Sort.Direction.DESC, "startTime");
        switch (type) {
            case OWNER:
                activitiesPage = activityRepo.findByPromoter(currentUser, pageRequest);
                if (activitiesPage.getTotalElements() < 1) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                activities = activitiesPage.getContent();
                for (Iterator<Activity> iterator = activities.iterator(); iterator.hasNext(); ) {
                    activity = iterator.next();
                    coverImage = activity.getCoverImage();
                    if (coverImage != null) {
                        activity.setCoverImageAccessToken(imageService.generateAccessToken(coverImage));
                    }
                }
                return new ResponseEntity<>(activities, HttpStatus.OK);
            case MEMBER:
                activitiesPage = activityRepo.findByPromoter(currentUser, pageRequest);
                if (activitiesPage.getTotalElements() < 1) {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
                activities = activitiesPage.getContent();
                for (Iterator<Activity> iterator = activities.iterator(); iterator.hasNext(); ) {
                    activity = iterator.next();
                    coverImage = activity.getCoverImage();
                    if (coverImage != null) {
                        activity.setCoverImageAccessToken(imageService.generateAccessToken(coverImage));
                    }
                }
                return new ResponseEntity<>(activities, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
