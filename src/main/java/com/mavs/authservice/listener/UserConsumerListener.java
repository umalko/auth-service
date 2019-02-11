package com.mavs.authservice.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.mavs.activity.dto.ActivityAuthUserDto;
import com.mavs.activity.dto.ActivityDto;
import com.mavs.activity.model.ActivityProcessType;
import com.mavs.activity.util.ActivityUtil;
import com.mavs.authservice.model.Authority;
import com.mavs.authservice.model.SecurityUserDetails;
import com.mavs.authservice.model.UserActivity;
import com.mavs.authservice.service.SecurityUserDetailsService;
import com.mavs.authservice.service.UserActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RabbitListener(queues = "NEW_USER_AUTH_QUEUE")
public class UserConsumerListener {

    private final UserActivityService activityService;
    private final SecurityUserDetailsService userService;

    public UserConsumerListener(UserActivityService activityService, SecurityUserDetailsService userService) {
        this.activityService = activityService;
        this.userService = userService;
    }

    @RabbitHandler
    public void onReceive(@Payload String jsonObject) {
        transformJsonObject(jsonObject, new TypeReference<ActivityDto<ActivityAuthUserDto>>() {
        }).ifPresent(activityDto ->
                ActivityUtil.convertToActivity(activityDto).ifPresent(activity -> {
                    activityService.save(new UserActivity(activity));
                    ActivityAuthUserDto objectDto = activityDto.getObjectDto();
                    userService.save(transformToUserDetails(objectDto));

                    activity.setProcessType(ActivityProcessType.PROCESSED);
                    activityService.save(new UserActivity(activity));
                }));
    }

    private <T> Optional<T> transformJsonObject(String jsonObject, TypeReference<T> typeReference) {
        try {
            T object = new ObjectMapper().readValue(jsonObject, typeReference);
            log.info("Activity object comes to Notification Service: {}", object);
            return Optional.of(object);
        } catch (IOException e) {
            log.error("Activity oject wasn't parsed correctly! Object: " + jsonObject, e);
        }
        return Optional.empty();
    }

    private SecurityUserDetails transformToUserDetails(ActivityAuthUserDto userDto) {
        return SecurityUserDetails.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .authorities(Sets.newHashSet(Authority.USER, Authority.ADMIN))
                .build();
    }
}
