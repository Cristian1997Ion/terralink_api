package com.terralink.terralink_api.http.api.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public final class ValidationErrorPayload {
    public final List<String> generalErrors;
    public final Map<String, ?> fieldErrors;

    public ValidationErrorPayload(List<ObjectError> generalErrors, List<FieldError> fieldErrors) {
        this.generalErrors = generalErrors
            .stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.toList());

        this.fieldErrors = fieldErrors
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                error -> new ArrayList<>(List.of(error.getDefaultMessage())),
                (error1, error2) -> { error1.addAll(error2); return error1; }
            ));
    }
}
