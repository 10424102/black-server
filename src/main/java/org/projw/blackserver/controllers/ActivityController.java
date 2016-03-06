package org.projw.blackserver.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.projw.blackserver.App;
import org.projw.blackserver.config.json.Views;
import org.projw.blackserver.config.CurrentUser;
import org.projw.blackserver.models.ActivityRecommendationRepo;
import org.projw.blackserver.models.ActivityRepo;
import org.projw.blackserver.models.*;

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
    @Autowired ObjectMapper objectMapper;

    /**
     * @api GET /activities
     *
     * 获取活动
     *
     * @param category 活动类别
     * <ul>
     *   <li> 推荐活动: recommendations
     *   <li> 校园活动: school
     *   <li> 朋友参加的活动: friends
     *   <li> 关注的人参加的活动: focuses
     *   <li> 自己参加的活动: myself
     * </ul>
     *
     * @param pageable 分页信息
     */
    @RequestMapping(method = GET)
    @Transactional(readOnly = true)
    public String getActivities(@RequestParam String category, Pageable pageable, @CurrentUser User user) throws JsonProcessingException{
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
        return objectMapper.writerWithView(Views.ActivitySummary.class).writeValueAsString(activities);
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
