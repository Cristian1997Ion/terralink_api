package com.terralink.terralink_api.http.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthHandler {

    public Mono<ServerResponse> login(ServerRequest request) {
        return ServerResponse.ok().body(BodyInserters.fromValue(""));
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return ServerResponse.ok().body(BodyInserters.fromValue(""));
    }
}
