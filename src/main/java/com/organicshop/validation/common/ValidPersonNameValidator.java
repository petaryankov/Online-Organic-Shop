package com.organicshop.validation.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidPersonNameValidator implements ConstraintValidator<ValidPersonName, String> {
    @Override
    public boolean isValid(String value,
                           ConstraintValidatorContext context) {
        //The name must be between 2-14 chars and start with capital letter
        String regex = "^[A-Z](?=.{1,15}$)[A-Za-z]*(?:\\h+[A-Z][A-Za-z]*)*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();

    }

}
