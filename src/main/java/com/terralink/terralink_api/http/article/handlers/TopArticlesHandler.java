package com.terralink.terralink_api.http.article.handlers;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;

import com.terralink.terralink_api.domain.article.service.ArticleService;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.article.payload.ArticleListPayload;

import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class TopArticlesHandler {
    private ArticleService articleService;

    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        return this.articleService
            .getTopInternalArticles()
            .flatMap(articles -> ServerResponse.ok().bodyValue(
                new ApiResponse<ArticleListPayload>(true, null, new ArticleListPayload(articles))
            ));
    }
}
