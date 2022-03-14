package com.terralink.terralink_api.http.api;

import com.terralink.terralink_api.domain.user.entity.User;
import com.terralink.terralink_api.domain.user.repository.UserRepository;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ApiHandler {

    private UserRepository userRepository;

    public Mono<ServerResponse> index(ServerRequest request) {

        return ServerResponse.ok().body(this.userRepository.findAll(), User.class);

        //return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
        //  .body(BodyInserters.fromValue(new ApiResponse(true, null)));
    }   
}
