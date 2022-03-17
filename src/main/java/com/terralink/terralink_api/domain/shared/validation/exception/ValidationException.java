package com.terralink.terralink_api.domain.shared.validation.exception;

import java.util.List;

import org.springframework.validation.ObjectError;

import lombok.Getter;

public class ValidationException extends Exception{
    public final static String DEFAULT_ERROR_MESSAGE = "Validation error!";

    @Getter
    private final List<ObjectError> violations;

    public ValidationException(List<ObjectError> violations) {
        super(DEFAULT_ERROR_MESSAGE);
        this.violations = violations;
    }

    public ValidationException(Class<?> objectClass, String message) {
        super(DEFAULT_ERROR_MESSAGE);
        this.violations = List.of(new ObjectError(objectClass.getName(), message));
    }
}
