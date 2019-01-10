package com.mavs.authservice.validation;


import com.mavs.authservice.dto.RegisterUserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        RegisterUserDto user = (RegisterUserDto) o;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}
