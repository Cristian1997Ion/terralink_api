package com.terralink.terralink_api.http.article.handler;

import com.terralink.terralink_api.domain.article.service.ArticleService;
import com.terralink.terralink_api.domain.user.entity.User;
import com.terralink.terralink_api.http.api.ApiResponse;
import com.terralink.terralink_api.http.api.validation.AbstractValidationHandler;
import com.terralink.terralink_api.http.article.request.CreateArticleRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

@Component
public class CreateArticleHandler extends AbstractValidationHandler<CreateArticleRequest, Validator> {
    private ArticleService articleService;

    public CreateArticleHandler(
        ArticleService articleService,
        @Autowired Validator validator
    ) {
        super(CreateArticleRequest.class, validator);
        this.articleService = articleService;
    }

    @Override
    protected Mono<ServerResponse> processBody(CreateArticleRequest validRequest, ServerRequest originalRequest) {
        return ReactiveSecurityContextHolder.getContext()
            .map(context -> (User) context.getAuthentication().getPrincipal())
            .flatMap(user -> this.articleService.createArticle(
                 validRequest.getTitle(),
                 user,
                 validRequest.getContent()
            ))
            .flatMap(article -> ServerResponse.status(HttpStatus.CREATED).bodyValue(new ApiResponse<>()));
    }
    
}
