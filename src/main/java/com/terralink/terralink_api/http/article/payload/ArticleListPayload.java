package com.terralink.terralink_api.http.article.payload;

import java.util.List;

import com.terralink.terralink_api.domain.article.dto.ArticleOut;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ArticleListPayload {
    @Getter
    private final List<ArticleOut> articles;
}
