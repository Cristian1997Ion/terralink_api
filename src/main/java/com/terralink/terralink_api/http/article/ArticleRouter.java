package com.terralink.terralink_api.http.article;

import com.terralink.terralink_api.http.article.handlers.TopArticlesHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class ArticleRouter {

    @Bean
    public RouterFunction<ServerResponse> articleRoutes (TopArticlesHandler topArticlesHandler) {
        return RouterFunctions.route().nest(RequestPredicates.path("/article"), builder -> 
            builder
                .GET("/top", topArticlesHandler::handleRequest)
        ).build();
    }
}
