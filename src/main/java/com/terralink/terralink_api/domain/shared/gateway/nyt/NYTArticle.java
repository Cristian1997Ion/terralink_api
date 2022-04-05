package com.terralink.terralink_api.domain.shared.gateway.nyt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NYTArticle {
    @Getter
    private String section;

    @Getter
    private String title;

    @Getter
    private String headline;

    @Getter
    private String fullArticleUrl;
}
