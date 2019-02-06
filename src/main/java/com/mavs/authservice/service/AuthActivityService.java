package com.mavs.authservice.service;

import com.mavs.authservice.model.User;

public interface AuthActivityService {

    void processNewUserActivity(User savedUser);
}
