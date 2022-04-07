package com.terralink.terralink_api.http.article.handler;

import com.terralink.terralink_api.domain.article.dto.ArticleOut;
import com.terralink.terralink_api.domain.article.service.ArticleService;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ArticleFeedHandler {
    private ArticleService articleService;

    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(this.articleService.getArticleFeed(), ArticleOut.class);
    }
}
