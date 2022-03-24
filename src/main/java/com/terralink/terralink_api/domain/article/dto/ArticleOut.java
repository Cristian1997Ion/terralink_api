package com.terralink.terralink_api.domain.article.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleOut {
    private Integer articleId;

    private String author;

    private String title;

    private String content;

    private Long numberOfLikes;
}
