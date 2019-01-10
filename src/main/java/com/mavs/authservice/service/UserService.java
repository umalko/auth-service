package com.mavs.authservice.service;


import com.mavs.authservice.dto.RegisterUserDto;
import com.mavs.authservice.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Integer id);

    Optional<User> findByName(String name);

    Optional<User> registerNewUser(RegisterUserDto user);

    boolean isUserPasswordValid(String userPassword, String encryptedPassword);

    void update(User user);

    void delete(Integer id);
}
