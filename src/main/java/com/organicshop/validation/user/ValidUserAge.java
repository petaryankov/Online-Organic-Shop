package com.organicshop.validation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.organicshop.constants.ValidationErrorMessages.INVALID_AGE;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidUserAgeValidator.class)
public @interface ValidUserAge {

    String message() default INVALID_AGE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
