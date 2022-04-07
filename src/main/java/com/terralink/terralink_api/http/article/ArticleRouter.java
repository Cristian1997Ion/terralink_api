package com.terralink.terralink_api.http.article;

import com.terralink.terralink_api.http.article.handler.ArticleFeedHandler;
import com.terralink.terralink_api.http.article.handler.CreateArticleHandler;
import com.terralink.terralink_api.http.article.handler.MixedArticlesHandler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class ArticleRouter {

    @Bean
    public RouterFunction<ServerResponse> articleRoutes (
        ArticleFeedHandler articleFeedHandler,
        MixedArticlesHandler mixedArticlesHandler,
        CreateArticleHandler createArticleHandler
    ) {
        return RouterFunctions.route().nest(RequestPredicates.path("/article"), builder -> 
            builder
                .GET("/feed", articleFeedHandler::handleRequest)
                .GET("/mixed", mixedArticlesHandler::handleRequest)
                .POST("/create", createArticleHandler::handleRequest)
        ).build();
    }
}
