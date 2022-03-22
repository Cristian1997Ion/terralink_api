package com.terralink.terralink_api.domain.article.service;

import java.util.List;

import com.terralink.terralink_api.domain.article.dto.ArticleOut;
import com.terralink.terralink_api.domain.article.repository.ArticleRepository;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class ArticleService {
    private ArticleRepository articleRepository;

    public Mono<List<ArticleOut>> getTopInternalArticles() {
        return this.articleRepository.findTopArticles(10);
    }
    
}
