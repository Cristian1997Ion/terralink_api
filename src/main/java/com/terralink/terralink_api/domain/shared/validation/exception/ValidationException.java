package com.terralink.terralink_api.domain.shared.validation.exception;

import java.util.List;

import org.springframework.validation.ObjectError;

import lombok.Getter;

public class ValidationException extends Exception{
    @Getter
    private final List<ObjectError> violations;

    public ValidationException(List<ObjectError> violations) {
        super("Validation error!");
        this.violations = violations;
    }
}
