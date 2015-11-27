package com.example.services;

import com.example.daos.ActivityRecommendationRepo;
import com.example.daos.ActivityRepo;
import com.example.models.Activity;
import com.example.models.ActivityRecommendation;
import com.example.models.Friendship;
import com.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired ActivityRepo activityRepo;

    @Autowired UserService userService;

    @Autowired ActivityRecommendationRepo activityRecommendationRepo;

    @Override
    public List<Activity> getRecommendedActivities(Pageable pageable) {
        Iterable<ActivityRecommendation> i = activityRecommendationRepo.findAll(pageable);
        List<Activity> activities = new ArrayList<>();
        for (ActivityRecommendation r : i) {
            activities.add(r.getActivity());
        }
        return activities;
    }

    @Override
    public List<Activity> getSameSchoolActivities(Pageable pageable) {
        User user = userService.getCurrentUser();
        return activityRepo.findByPromoterProfileCollege(user.getProfile().getCollege(), pageable);
    }

    @Override
    public List<Activity> getFriendsActivities(Pageable pageable) {
        User user = userService.getCurrentUser();
        Set<User> friends = user.getFriendshipSet().stream().map(Friendship::getFriend).collect(Collectors.toSet());
        return activityRepo.findByPromoterIn(friends, pageable);
    }

    @Override
    public List<Activity> getFocusesActivities(Pageable pageable) {
        User user = userService.getCurrentUser();
        Set<User> focuses = user.getFocuses();
        return activityRepo.findByPromoterIn(focuses, pageable);
    }

    @Override
    public List<Activity> getMyActivities(Pageable pageable) {
        User user = userService.getCurrentUser();
        return activityRepo.findByPromoter(user, pageable);
    }

    @Override
    public void createActivity(Activity activity) {
        activityRepo.save(activity);
    }

    @Override
    public void deleteActivity(long activityId) {
        activityRepo.delete(activityId);
    }

    @Override
    public Activity findOne(long activityId) {
        return activityRepo.findOne(activityId);
    }
}
