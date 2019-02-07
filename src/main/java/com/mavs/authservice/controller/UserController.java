package com.mavs.authservice.controller;

import com.google.common.base.Preconditions;
import com.mavs.authservice.dto.ResponseUserDto;
import com.mavs.authservice.exception.ResourceNotFoundException;
import com.mavs.authservice.model.User;
import com.mavs.authservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<ResponseUserDto> findAll() {
        return userService.findAll().stream().map(this::transformUserModelToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseUserDto findById(@PathVariable("id") Integer id) {
        log.info("requested ID: {}", id);
        return transformUserOptionalModelToDto(userService.findById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseUserDto findByName(@PathVariable("username") String username) {
        return transformUserOptionalModelToDto(userService.findByName(username));
    }

    @GetMapping("/email/{email}")
    public ResponseUserDto findByEmail(@PathVariable("email") String email) {
        return transformUserOptionalModelToDto(userService.findByEmail(email));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody User user) {
        Preconditions.checkNotNull(user);
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        userService.delete(id);
    }

    private ResponseUserDto transformUserModelToDto(User user) {
        return ResponseUserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .phone(user.getPhone())
                .build();
    }

    private ResponseUserDto transformUserOptionalModelToDto(Optional<User> userOptional) {
        if (userOptional.isPresent()) {
            return transformUserModelToDto(userOptional.get());
        }
        throw new ResourceNotFoundException();
    }
}

