package com.terralink.terralink_api.http.auth;

import com.terralink.terralink_api.domain.auth.service.JWTService;
import com.terralink.terralink_api.domain.user.service.UserService;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.auth.payload.LoginPayload;
import com.terralink.terralink_api.http.auth.request.LoginRequest;
import com.terralink.terralink_api.http.auth.request.RegisterRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthHandler {

    private UserService userService;

    private JWTService jwtService;

    public Mono<ServerResponse> login(ServerRequest request) {
        return request
            .bodyToMono(LoginRequest.class)
            .defaultIfEmpty(new LoginRequest())
            .flatMap(loginRequest -> this.userService.findUserByCredentials(loginRequest.getUsername(), loginRequest.getPassword()))
            .flatMap(user -> ServerResponse.ok().bodyValue(
                new ApiResponse(true, null, new LoginPayload(user.getUsername(), jwtService.generateToken(user)))
            ));
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request
            .bodyToMono(RegisterRequest.class)
            .defaultIfEmpty(new RegisterRequest())
            .flatMap(registerRequest -> this.userService.createUser(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                true
            ))
            .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(new ApiResponse()));
    }
}
