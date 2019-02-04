package com.mavs.authservice.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mavs.activity.model.Activity;
import com.mavs.activity.model.ActivityType;
import com.mavs.activity.provider.ActivityMessageQueueProvider;
import com.mavs.activity.service.ActivityService;
import com.mavs.activity.util.ActivityUtil;
import com.mavs.authservice.dto.ActivityUserDto;
import com.mavs.authservice.dto.RegisterUserDto;
import com.mavs.authservice.exception.ResourceWasNotSavedException;
import com.mavs.authservice.model.Authority;
import com.mavs.authservice.model.SecurityUserDetails;
import com.mavs.authservice.model.User;
import com.mavs.authservice.repository.UserRepository;
import com.mavs.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ActivityService<User> activityService;
    private final ActivityMessageQueueProvider activityMessageQueueProvider;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, ActivityService<User> activityService, ActivityMessageQueueProvider activityMessageQueueProvider) {
        this.userRepository = userRepository;
        this.activityService = activityService;
        this.activityMessageQueueProvider = activityMessageQueueProvider;
    }

    @Override
    public List<User> findAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @Override
    @Cacheable(value = "users", key = "#userId", unless = "#result == null")
    public Optional<User> findById(Integer userId) {
        log.info("Find user by id: {}", userId);
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public Optional<User> registerNewUser(RegisterUserDto registerUserDto) {
        if (userRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
            throw new ResourceWasNotSavedException();
        }

        User user = transformToUser(registerUserDto);
        user.getSecurityUserDetails().setPassword(encoder.encode(user.getSecurityUserDetails().getPassword()));
        User savedUser = userRepository.save(user);

        processNewUserActivity(savedUser);
        return Optional.of(savedUser);
    }

    @Override
    @CachePut(value = "users", key = "#user.id", unless = "#result == null")
    public void update(User user) {
        userRepository.findById(user.getId()).ifPresent(dbUser -> {
            BeanUtils.copyProperties(user, dbUser);
            userRepository.save(dbUser);
        });
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public void delete(Integer userId) {
        userRepository.deleteById(userId);
    }

    private String encryptPassword(String password) {
        return password;
    }

    private User transformToUser(RegisterUserDto registerUserDto) {
        return User.builder()
                .email(registerUserDto.getEmail())
                .username(registerUserDto.getUsername())
                .securityUserDetails(
                        SecurityUserDetails.builder()
                                .username(registerUserDto.getUsername())
                                .password(encryptPassword(registerUserDto.getPassword()))
                                .authorities(Sets.newHashSet(Authority.USER, Authority.ADMIN))
                                .build()).build();
    }

    private void processNewUserActivity(User savedUser) {
        ActivityUtil.buildActivity(buildActivityUserDto(savedUser), ActivityType.USER).ifPresent(activity -> {
            Activity savedActivity = activityService.save(activity);
            activityMessageQueueProvider.produceActivity(savedActivity);
        });
    }

    private ActivityUserDto buildActivityUserDto(User savedUser) {
        return ActivityUserDto.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .phone(savedUser.getPhone())
                .username(savedUser.getUsername())
                .build();
    }
}
