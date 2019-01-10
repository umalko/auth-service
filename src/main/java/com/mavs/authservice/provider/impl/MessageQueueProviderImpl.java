package com.mavs.authservice.provider.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mavs.authservice.model.User;
import com.mavs.authservice.provider.MessageQueueProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageQueueProviderImpl implements MessageQueueProvider {

    private static final String USER_QUEUE = "USER_QUEUE";

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public MessageQueueProviderImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendToUserTopic(User user) {
        try {
            String json = new ObjectMapper().writeValueAsString(user);
            rabbitTemplate.convertAndSend(USER_QUEUE, json);
        } catch (Exception e) {
            log.error("Something wrong!", e);
        }
    }
}
