package com.organicshop.validation.user;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {

        //username must be 4 to 10 symbols and starts with letter
        String regex = "^[A-Za-z]\\w{3,10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();

    }
}
