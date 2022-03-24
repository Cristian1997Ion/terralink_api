package com.terralink.terralink_api.http.auth.handler;

import com.terralink.terralink_api.domain.auth.service.JWTService;
import com.terralink.terralink_api.domain.shared.validation.exception.ValidationException;
import com.terralink.terralink_api.domain.user.service.UserService;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.api.validation.AbstractValidationHandler;
import com.terralink.terralink_api.http.auth.payload.LoginPayload;
import com.terralink.terralink_api.http.auth.request.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class LoginHandler extends AbstractValidationHandler<LoginRequest, Validator> {
    private UserService userService;
    private JWTService jwtService;

    public LoginHandler(UserService userService, JWTService jwtService, @Autowired Validator validator) {
        super(LoginRequest.class, validator);
        this.userService = userService;
        this.jwtService  = jwtService;
    }

    @Override
    protected Mono<ServerResponse> processBody(
        LoginRequest validLoginRequest,
        ServerRequest originalRequest
    ) {
        return Mono
            .just(validLoginRequest)
            .flatMap(loginRequest -> this.userService.findUserByCredentials(loginRequest.getUsername(), loginRequest.getPassword()))
            .switchIfEmpty(Mono.error(
                new ValidationException(
                    LoginRequest.class,
                    "Invalid credentials"
                )
            ))
            .flatMap(user -> ServerResponse.ok().bodyValue(
                new ApiResponse<LoginPayload>(true, null, new LoginPayload(user.getUsername(), jwtService.generateToken(user)))
            ));
    }
}
