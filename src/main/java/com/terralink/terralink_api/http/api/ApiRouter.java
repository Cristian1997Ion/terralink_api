package com.terralink.terralink_api.http.api;

import com.terralink.terralink_api.http.api.handler.ApiHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public final class ApiRouter {
    
    @Bean
    public RouterFunction<ServerResponse> apiRoutes(ApiHandler apiHandler) {

        return RouterFunctions
            .route()
            .GET("/", apiHandler::index)
            .build();
      }
}
