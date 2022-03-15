package com.terralink.terralink_api.http.auth;

import com.terralink.terralink_api.domain.user.service.UserService;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.auth.request.RegisterRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthHandler {

    private UserService userService;

    public Mono<ServerResponse> login(ServerRequest request) {
        return ServerResponse.ok().body(BodyInserters.fromValue(""));
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request
            .bodyToMono(RegisterRequest.class)
            .defaultIfEmpty(new RegisterRequest())
            .flatMap(registerRequest -> {
                return this.userService.createUser(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                registerRequest.getPassword(),
                true
                );
            })
            .flatMap(user -> ServerResponse.status(HttpStatus.CREATED).bodyValue(new ApiResponse()));
    }
}
