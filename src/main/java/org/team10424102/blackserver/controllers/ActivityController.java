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
import org.team10424102.blackserver.models.Activity;
import org.team10424102.blackserver.services.ActivityService;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ActivityController {

    public static final String TYPE_RECOMMENDATIONS = "recommendations";
    public static final String TYPE_SCHOOL = "school";
    public static final String TYPE_FRIENDS = "friends";
    public static final String TYPE_FOCUSES = "focuses";
    public static final String TYPE_MYSELF = "myself";

    @Autowired ActivityService activityService;

    /**
     * 获取活动
     *
     * @param category recommendations|school|friends|focuses|myself
     *                 推荐活动, 校园活动, 朋友活动, 关注的人活动, 自己参加的活动
     */
    @RequestMapping(value = App.API_ACTIVITY, method = GET)
    @JsonView(Views.ActivitySummary.class)
    public List<Activity> getActivities(@RequestParam String category, Pageable pageable) {
        List<Activity> activities = null;
        switch (category.toLowerCase()) {
            case TYPE_RECOMMENDATIONS:
                activities = activityService.getRecommendedActivities(pageable);
                break;
            case TYPE_SCHOOL:
                activities = activityService.getSameSchoolActivities(pageable);
                break;
            case TYPE_FRIENDS:
                activities = activityService.getFriendsActivities(pageable);
                break;
            case TYPE_FOCUSES:
                activities = activityService.getFocusesActivities(pageable);
                break;
            case TYPE_MYSELF:
                activities = activityService.getMyActivities(pageable);
                break;
        }
        return activities;
    }

    /**
     * 获取活动的详细信息
     */
    @RequestMapping(value = App.API_ACTIVITY + "/{id}", method = GET)
    @JsonView(Views.ActivityDetails.class)
    public Activity getActivity(@PathVariable long id) {
        return activityService.findOne(id);
    }

    /**
     * @param activity
     * @return
     */
    @RequestMapping(value = App.API_ACTIVITY, method = POST)
    public void createActivity(Activity activity) {
        activityService.createActivity(activity);
    }

    @RequestMapping(value = App.API_ACTIVITY + "/{id}", method = DELETE)
    @JsonView(Views.ActivityDetails.class)
    public void deleteActivity(@PathVariable long id) {
        activityService.deleteActivity(id);
    }
}
