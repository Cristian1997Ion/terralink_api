package com.terralink.terralink_api.domain.shared.gateway.nyt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class NYTResponse {
    @Getter
    private String status;

    @Getter
    private Integer articlesNumber;

    @Getter
    private List<NYTArticle> articles;
}
