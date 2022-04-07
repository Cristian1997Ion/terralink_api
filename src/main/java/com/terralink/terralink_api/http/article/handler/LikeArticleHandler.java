package com.terralink.terralink_api.http.article.handler;

import com.terralink.terralink_api.domain.article.service.ArticleService;
import com.terralink.terralink_api.domain.user.entity.User;
import com.terralink.terralink_api.http.api.ApiResponse;

import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class LikeArticleHandler {
    private ArticleService articleService;

    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        return ServerResponse.ok().body(
            ReactiveSecurityContextHolder.getContext()
                .map(context -> (User) context.getAuthentication().getPrincipal())
                .flatMap(user ->
                    this.articleService.likeArticleById(
                        user,
                        Integer.valueOf(request.pathVariable("id"))
                    )
                )
                .map(saved -> new ApiResponse<>()),
            ApiResponse.class
        );
    }
}
