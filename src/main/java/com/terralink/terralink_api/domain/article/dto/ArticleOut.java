package com.terralink.terralink_api.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class ArticleOut {
    private Integer articleId;

    private String author;

    private String title;

    private String content;

    private Long numberOfLikes;
}
