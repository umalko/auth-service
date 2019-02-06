package com.mavs.authservice.service.impl;

import com.mavs.activity.dto.ActivityDto;
import com.mavs.activity.dto.ActivityUserDto;
import com.mavs.activity.model.Activity;
import com.mavs.activity.model.ActivityProcessType;
import com.mavs.activity.model.ActivityType;
import com.mavs.activity.provider.ActivityMessageQueueProvider;
import com.mavs.activity.util.ActivityUtil;
import com.mavs.authservice.model.User;
import com.mavs.authservice.model.UserActivity;
import com.mavs.authservice.service.AuthActivityService;
import com.mavs.authservice.service.UserActivityService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class AuthActivityServiceImpl implements AuthActivityService {

    private final UserActivityService userActivityService;
    private final ActivityMessageQueueProvider activityMessageQueueProvider;

    public AuthActivityServiceImpl(UserActivityService userActivityService, ActivityMessageQueueProvider activityMessageQueueProvider) {
        this.userActivityService = userActivityService;
        this.activityMessageQueueProvider = activityMessageQueueProvider;
    }

    @Async
    @Override
    public void processNewUserActivity(User savedUser) {
        ActivityUserDto activityUserDto = buildActivityUserDto(savedUser);
        ActivityUtil.buildActivity(activityUserDto, ActivityType.USER).ifPresent(activity -> {
            Activity savedActivity = userActivityService.save(new UserActivity(activity));
            ActivityDto activityDto = ActivityUtil.buildActivityDto(savedActivity, activityUserDto);
            activityMessageQueueProvider.produceActivity(activityDto);

            savedActivity.setProcessType(ActivityProcessType.SENT);
            userActivityService.update(new UserActivity(savedActivity));
        });
    }

    private ActivityUserDto buildActivityUserDto(User savedUser) {
        return ActivityUserDto.builder()
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .username(savedUser.getUsername())
                .build();
    }
}
