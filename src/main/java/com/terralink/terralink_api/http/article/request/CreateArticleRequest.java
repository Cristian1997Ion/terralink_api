package com.terralink.terralink_api.http.article.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateArticleRequest {
    @NotNull
    @Size(min = 4, max = 32)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "must be alphanumerical")
    private String title;

    @NotNull
    @Size(min = 1)
    private String content;
}
