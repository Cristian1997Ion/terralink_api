package com.terralink.terralink_api.http.article.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;

import com.terralink.terralink_api.domain.article.service.ArticleService;
import com.terralink.terralink_api.domain.shared.gateway.nyt.NYTCategory;
import com.terralink.terralink_api.domain.shared.gateway.nyt.NYTGateway;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.article.payload.MixedArticlesPayload;

import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class MixedArticlesHandler {
    private ArticleService articleService;
    private NYTGateway nytGateway;

    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        Mono<ApiResponse<MixedArticlesPayload>> responseMono = Mono.zip(
            this.articleService.getTopInternalArticles(),
            this.nytGateway.getRecentNews(NYTCategory.FOOD),
            this.nytGateway.getRecentNews(NYTCategory.HEALTH)
        ).map(responses -> new ApiResponse<MixedArticlesPayload>(
            true,
            null,
            new MixedArticlesPayload(
                responses.getT1(),
                responses.getT2().getArticles().subList(0, 5),
                responses.getT3().getArticles().subList(0, 5)
            )
        ));

        return ServerResponse.ok().body(responseMono, ApiResponse.class);
    }
}
