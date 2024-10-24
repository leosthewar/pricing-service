package com.capitole.pricingservice.common.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.util.Set;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;


public class BeanValidation {
    private final static Validator validator = buildDefaultValidatorFactory().getValidator();

    /**
     * Evaluates all Bean Validation annotations on the subject.
     */
    public static <T> void validate(T subject) {
        Set<ConstraintViolation<T>> violations = validator.validate(subject);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
