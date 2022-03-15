package com.terralink.terralink_api.http.api;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ApiHandler {
    public Mono<ServerResponse> index(ServerRequest request) {
        return ServerResponse
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(new ApiResponse()));
    }   
}
