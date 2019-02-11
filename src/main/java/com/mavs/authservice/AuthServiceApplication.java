package com.mavs.authservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableRabbit
@EnableAsync
@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"com.mavs.activity.provider.*", "com.mavs.authservice.*"})
@EntityScan("com.mavs.authservice.*")
@EnableJpaRepositories("com.mavs.authservice.*")
public class AuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}

