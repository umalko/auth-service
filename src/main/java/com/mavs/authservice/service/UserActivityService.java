package com.mavs.authservice.service;


import com.mavs.activity.model.ActivityType;
import com.mavs.authservice.model.UserActivity;

import java.util.List;
import java.util.Optional;

public interface UserActivityService {

    /**
     * Get activity by id
     *
     * @param id activity id
     * @return activity object
     */
    Optional<UserActivity> getById(String id);

    /**
     * Find all activities stored in the current service
     *
     * @return list of activities
     */
    List<UserActivity> getAll();

    /**
     * Get activities by specific type
     *
     * @param type activity type(User,...)
     * @return list of activities filtered by activity type
     */
    List<UserActivity> getByType(ActivityType type);

    /**
     * Save activity
     *
     * @param activity activity object
     * @return saved activity
     */
    UserActivity save(UserActivity activity);

    /**
     * Update existing activity
     *
     * @param activity activity object
     * @return modified activity
     */
    UserActivity update(UserActivity activity);

}
