package com.terralink.terralink_api.domain.article.service;

import java.util.List;
import java.util.Set;

import com.terralink.terralink_api.domain.article.dto.ArticleOut;
import com.terralink.terralink_api.domain.article.entity.Article;
import com.terralink.terralink_api.domain.article.repository.ArticleRepository;
import com.terralink.terralink_api.domain.user.entity.User;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Component
public class ArticleService {
    private ArticleRepository articleRepository;
    private Sinks.Many<ArticleOut> articleFeedSink;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        this.articleFeedSink = Sinks.many().multicast().directAllOrNothing();
    }

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
        )).doOnSuccess(article -> {
            this.articleFeedSink.tryEmitNext(new ArticleOut(
                article.getId(),
                article.getAuthor().getUsername(),
                article.getTitle(),
                article.getContent(),
                (long) 0
            ));
        });
    }

    public Flux<ArticleOut> getArticleFeed() {
        return this.articleFeedSink.asFlux();
    }
    
}
