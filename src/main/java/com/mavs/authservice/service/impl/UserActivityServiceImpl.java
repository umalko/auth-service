package com.mavs.authservice.service.impl;

import com.google.common.collect.Lists;
import com.mavs.activity.model.ActivityType;
import com.mavs.authservice.model.UserActivity;
import com.mavs.authservice.repository.UserActivityRepository;
import com.mavs.authservice.service.UserActivityService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityRepository userActivityRepository;

    public UserActivityServiceImpl(UserActivityRepository userActivityRepository) {
        this.userActivityRepository = userActivityRepository;
    }

    @Override
    public Optional<UserActivity> getById(String id) {
        return userActivityRepository.findById(id);
    }

    @Override
    public List<UserActivity> getAll() {
        return Lists.newArrayList(userActivityRepository.findAll());
    }

    @Override
    public List<UserActivity> getByType(ActivityType type) {
        return userActivityRepository.findByType(type.name());
    }

    @Override
    public UserActivity save(UserActivity activity) {
        return userActivityRepository.save(activity);
    }

    @Override
    public UserActivity update(UserActivity activity) {
        Optional<UserActivity> dbActivityOpt = userActivityRepository.findById(activity.getId());
        if (dbActivityOpt.isPresent()) {
            UserActivity dbActivity = dbActivityOpt.get();
            BeanUtils.copyProperties(activity, dbActivity);
            return userActivityRepository.save(dbActivity);
        } else {
            return userActivityRepository.save(activity);
        }
    }
}
