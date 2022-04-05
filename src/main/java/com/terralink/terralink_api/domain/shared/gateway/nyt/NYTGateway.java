package com.terralink.terralink_api.domain.shared.gateway.nyt;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class NYTGateway {
    private WebClient webClient;
    private String apiUrl;
    private String secretKey;

    public NYTGateway(WebClient webClient, String apiUrl, String secretKey) {
        this.webClient = webClient;
        this.apiUrl = apiUrl;
        this.secretKey = secretKey;
    }

    public Mono<NYTResponse> getRecentNews(NYTCategory category) {
        switch (category) {
            case FOOD:
                return this.webClient
                    .get()
                    .uri(this.apiUrl + "/news/v3/content/all/food.json?api-key=" + this.secretKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(this::convertResponse);

            case HEALTH:
                return this.webClient
                    .get()
                    .uri(this.apiUrl + "/news/v3/content/all/health.json?api-key=" + this.secretKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(this::convertResponse);

            default:
            case ALL:
                return this.webClient
                    .get()
                    .uri(this.apiUrl + "/news/v3/content/all/all.json?api-key=" + this.secretKey)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(this::convertResponse);              
        }
    }

    private NYTResponse convertResponse(Map<String, Object> rawResponse) {
        String status = rawResponse.get("status").toString();
        Integer articlesNumber = Integer.valueOf(rawResponse.get("num_results").toString());
        List<Map<String, ?>> rawArticles = (List<Map<String, ?>>) rawResponse.get("results");

        return new NYTResponse(
            status,
            articlesNumber,
            rawArticles
                .stream()
                .map(rawArticle -> new NYTArticle(
                    rawArticle.get("section").toString(),
                    rawArticle.get("title").toString(),
                    rawArticle.get("abstract").toString(),
                    rawArticle.get("url").toString()
                )).collect(Collectors.toList())
        );
    }
}
