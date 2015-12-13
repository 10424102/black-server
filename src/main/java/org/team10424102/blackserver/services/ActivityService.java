package org.team10424102.blackserver.services;

import org.springframework.data.domain.Pageable;
import org.team10424102.blackserver.models.Activity;
import org.team10424102.blackserver.models.Post;

import java.util.List;

public interface ActivityService {

    List<Activity> getRecommendedActivities(Pageable pageable);

    List<Activity> getSameSchoolActivities(Pageable pageable);

    List<Activity> getFriendsActivities(Pageable pageable);

    List<Activity> getFocusesActivities(Pageable pageable);

    List<Activity> getMyActivities(Pageable pageable);

    void createActivity(Activity activity);

    void deleteActivity(long activityId);

    Activity findOne(long activityId);

    void likeActivity(long activityId);

    void unlikeActivity(long activityId);

    List<Post> getComments(Pageable pageabl, long postId);

    void saveComment(long activityId, String content);

    void deleteComment(long activityId, long commentId);
}
