package com.terralink.terralink_api.domain.article.repository;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import com.terralink.terralink_api.domain.article.dto.ArticleOut;
import com.terralink.terralink_api.domain.article.entity.Article;
import com.terralink.terralink_api.domain.article.entity.Article_;
import com.terralink.terralink_api.domain.shared.repository.BaseEntityRepository;
import com.terralink.terralink_api.domain.user.entity.User;
import com.terralink.terralink_api.domain.user.entity.User_;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import reactor.core.publisher.Mono;

@Component
public class ArticleRepository extends BaseEntityRepository<Article> { 
    
    public ArticleRepository(Mutiny.SessionFactory sessionFactory) {
        super(sessionFactory, Article.class);
    }

    public Mono<List<ArticleOut>> findTopArticles(int limit) {
        CriteriaBuilder cb = this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<Article> root = query.from(Article.class);
        Join<Article, User> likedByJoin = root.join(Article_.likedBy);
        Join<Article, User> authorJoin = root.join(Article_.author);
        String numberOfLikesColumn = "number_of_likes";

        query
            .multiselect(List.of(
                root.get(Article_.id).alias(Article_.ID),
                authorJoin.get(User_.username).alias(User_.USERNAME),
                root.get(Article_.title).alias(Article_.TITLE),
                root.get(Article_.content).alias(Article_.CONTENT),
                cb.count(likedByJoin).alias(numberOfLikesColumn)
            ))
            .orderBy(cb.desc(cb.count(likedByJoin)))
            .groupBy(root.get(Article_.id), authorJoin.get(User_.username));

        return this.sessionFactory
            .withSession(session ->
                session
                    .createQuery(query)
                    .setMaxResults(limit)
                    .getResultList()
            )
            .convert()
            .with(UniReactorConverters.toMono())
            .defaultIfEmpty(List.of())
            .flatMap(rawArticles -> Mono.just(
                rawArticles
                    .stream()
                    .map(rawArticle -> new ArticleOut(
                        rawArticle.get(Article_.ID, Integer.class),
                        rawArticle.get(User_.USERNAME, String.class),
                        rawArticle.get(Article_.TITLE, String.class),
                        rawArticle.get(Article_.CONTENT, String.class),
                        rawArticle.get(numberOfLikesColumn, Long.class)
                    ))
                    .collect(Collectors.toList())
            ));
    }
}
