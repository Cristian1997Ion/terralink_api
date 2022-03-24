package com.terralink.terralink_api.http.auth.handler;

import com.terralink.terralink_api.domain.user.service.UserService;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.api.validation.AbstractValidationHandler;
import com.terralink.terralink_api.http.auth.request.RegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class RegisterHandler extends AbstractValidationHandler<RegisterRequest, Validator> {
    private UserService userService;

    public RegisterHandler(UserService userService, @Autowired Validator validator) {
        super(RegisterRequest.class, validator);
        this.userService = userService;
    }

    @Override
    protected Mono<ServerResponse> processBody(
        RegisterRequest validLoginRequest,
        ServerRequest originalRequest
    ) {
        return Mono
            .just(validLoginRequest)
            .flatMap(registerRequest -> this.userService.createUser(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                true
            ))
            .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(new ApiResponse<>()));
    }
}
