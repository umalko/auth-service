package com.mavs.authservice.provider;


import com.mavs.authservice.model.User;

public interface MessageQueueProvider {

    void sendToUserTopic(User user);
}
