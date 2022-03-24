package com.terralink.terralink_api.domain.article.service;

import java.util.List;
import java.util.Set;

import com.terralink.terralink_api.domain.article.dto.ArticleOut;
import com.terralink.terralink_api.domain.article.entity.Article;
import com.terralink.terralink_api.domain.article.repository.ArticleRepository;
import com.terralink.terralink_api.domain.user.entity.User;

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

    public Mono<Article> createArticle(
        String title,
        User author,
        String content
    ) {
        return this.articleRepository.save(new Article(
            null,
            title,
            author,
            content,
            Set.of()
        ));
    }
    
}
