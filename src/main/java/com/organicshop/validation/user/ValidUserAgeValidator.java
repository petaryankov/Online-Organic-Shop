package com.organicshop.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidUserAgeValidator implements ConstraintValidator<ValidUserAge, Integer> {

    @Override
    public boolean isValid(Integer age, ConstraintValidatorContext constraintValidatorContext) {

        if (age == null) {
            return false;
        }

        return age >= 18;
    }
}
