package com.terralink.terralink_api.domain.article.repository;
import com.terralink.terralink_api.domain.article.entity.Article;
import com.terralink.terralink_api.domain.shared.repository.BaseEntityRepository;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

@Component
public class ArticleRepository extends BaseEntityRepository<Article> { 
    
    public ArticleRepository(Mutiny.SessionFactory sessionFactory) {
        super(sessionFactory, Article.class);
    }

}
