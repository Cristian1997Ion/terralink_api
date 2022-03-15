package com.terralink.terralink_api.http.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class AuthRouter {
    
    @Bean
    public RouterFunction<ServerResponse> authRoutes(AuthHandler apiHandler) {

        return RouterFunctions.route().nest(RequestPredicates.path("/auth"), builder -> {
            builder
                .POST("/login", apiHandler::login)
                .POST("/register", apiHandler::register);
        }).build();
    }
}
