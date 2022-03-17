package com.terralink.terralink_api.http.api.validation;

import java.util.List;

import com.terralink.terralink_api.domain.shared.validation.exception.ValidationException;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public abstract class AbstractValidationHandler<T, U extends Validator> {
    private final Class<T> validationClass;
    private final U validator;

    protected AbstractValidationHandler(Class<T> clazz, U validator) {
        this.validationClass = clazz;
        this.validator = validator;
    }

    public final Mono<ServerResponse> handleRequest(final ServerRequest request) {
        return request
            .bodyToMono(this.validationClass)
            .switchIfEmpty(Mono.error(new ValidationException(
                List.of(new ObjectError(this.validationClass.getName(), "Invalid request body!"))
            )))
            .flatMap(body -> {
                Errors errors = new BeanPropertyBindingResult(
                    body,
                    this.validationClass.getName()
                );

                this.validator.validate(body, errors);
    
                if (errors == null || errors.getAllErrors().isEmpty()) {
                    return processBody(body, request);
                }
                
                return Mono.error(new ValidationException(errors.getAllErrors()));
            });
    }

    protected abstract Mono<ServerResponse> processBody(T validBody, ServerRequest originalRequest);
}