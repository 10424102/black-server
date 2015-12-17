package org.team10424102.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team10424102.blackserver.App;
import org.team10424102.blackserver.config.json.Views;
import org.team10424102.blackserver.config.security.CurrentUser;
import org.team10424102.blackserver.models.ActivityRecommendationRepo;
import org.team10424102.blackserver.models.ActivityRepo;
import org.team10424102.blackserver.models.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping(App.API_ACTIVITY)
public class ActivityController {
    public static final String TYPE_RECOMMENDATIONS = "recommendations";
    public static final String TYPE_SCHOOL = "school";
    public static final String TYPE_FRIENDS = "friends";
    public static final String TYPE_FOCUSES = "focuses";
    public static final String TYPE_MYSELF = "myself";

    @Autowired ActivityRecommendationRepo activityRecommendationRepo;

    @Autowired ActivityRepo activityRepo;

    /**
     * 获取活动
     *
     * @param category recommendations|school|friends|focuses|myself
     *                 推荐活动, 校园活动, 朋友活动, 关注的人活动, 自己参加的活动
     */
    @RequestMapping(method = GET)
    @JsonView(Views.ActivitySummary.class)
    public List<Activity> getActivities(@RequestParam String category, Pageable pageable, @CurrentUser User user) {
        List<Activity> activities = null;
        switch (category.toLowerCase()) {
            case TYPE_RECOMMENDATIONS:
                Iterable<ActivityRecommendation> i = activityRecommendationRepo.findAll(pageable);
                activities = new ArrayList<>();
                for (ActivityRecommendation r : i) {
                    activities.add(r.getActivity());
                }
                break;
            case TYPE_SCHOOL:
                activities = activityRepo.findByPromoterCollege(user.getCollege(), pageable);
                break;
            case TYPE_FRIENDS:
                activities = activityRepo.findByPromoterIn(user.getFriends(), pageable);
                break;
            case TYPE_FOCUSES:
                activities = activityRepo.findByPromoterIn(user.getFocuses(), pageable);
                break;
            case TYPE_MYSELF:
                activities = activityRepo.findByPromoter(user, pageable);
                break;
        }
        return activities;
    }

    /**
     * 获取活动的详细信息
     */
    @RequestMapping(value = "/{id}", method = GET)
    @JsonView(Views.ActivityDetails.class)
    public Activity getActivity(@PathVariable(value = "id") Activity activity) {
        return activity;
    }


    /**
     * 创建活动
     */
    @RequestMapping(method = POST)
    public void createActivity(@Valid Activity activity) {
        activityRepo.save(activity);
    }

    /**
     * 删除活动
     * @param id
     */
    @RequestMapping(value = "/{id}", method = DELETE)
    @JsonView(Views.ActivityDetails.class)
    public void deleteActivity(@PathVariable long id) {
       activityRepo.delete(id);
    }

    /**
     * 获得评论
     */
    @RequestMapping(value = "/{id}/comments", method = GET)
    @JsonView(Views.PostComment.class)
    public Collection<Post> getComments(@PathVariable long id, Pageable pageable) {
        return activityRepo.findCommentsById(id, pageable);
    }

    /**
     * 评论
     */
    @RequestMapping(value = "/{id}/comments", method = POST)
    public void addComment(@PathVariable(value = "id") Activity activity, @RequestParam String content, @CurrentUser User user) {
        Post comment = new Post(user, content, false);
        activity.getComments().add(comment);
        activityRepo.save(activity);
    }


}
