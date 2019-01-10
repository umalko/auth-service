package com.mavs.authservice.controller;

import com.mavs.authservice.dto.RegisterUserDto;
import com.mavs.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public String register(@RequestBody @Valid RegisterUserDto registerUserDto, BindingResult result) {
        if (!result.hasErrors()) {
            userService.registerNewUser(registerUserDto);
            return "User was created";
        }
        return "User wasn't created! Wrong params!";
    }
}
