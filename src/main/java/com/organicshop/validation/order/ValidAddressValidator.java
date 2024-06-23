package com.organicshop.validation.order;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidAddressValidator implements ConstraintValidator<ValidAddress, String> {
    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {

        return value.trim().length() >= 5 && value.trim().length() <= 50;
    }
}
