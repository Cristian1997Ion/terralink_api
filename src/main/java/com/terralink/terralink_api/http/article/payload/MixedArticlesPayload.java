package com.terralink.terralink_api.http.article.payload;

import java.util.List;

import com.terralink.terralink_api.domain.article.dto.ArticleOut;
import com.terralink.terralink_api.domain.shared.gateway.nyt.NYTArticle;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MixedArticlesPayload {
    @Getter
    private final List<ArticleOut> internalArticles;

    @Getter
    private final List<NYTArticle> nytFoodArticles;

    @Getter
    private final List<NYTArticle> nytHealthArticles;
}
