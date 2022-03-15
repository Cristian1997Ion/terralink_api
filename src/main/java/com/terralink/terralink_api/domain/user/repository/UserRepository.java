package com.terralink.terralink_api.domain.user.repository;
]
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.terralink.terralink_api.domain.shared.repository.BaseEntityRepository;
import com.terralink.terralink_api.domain.user.entity.User;
import com.terralink.terralink_api.domain.user.entity.User_;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import reactor.core.publisher.Mono;

@Component
public class UserRepository extends BaseEntityRepository<User> { 
    
    public UserRepository(Mutiny.SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    public Mono<User> findByCredentials(String username, String password) {
        CriteriaBuilder criteriaBuilder =  this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(entityClass);
        Root<User> root = query.from(this.entityClass);
        query.where(criteriaBuilder.and(
            criteriaBuilder.equal(root.get(User_.username), username),
            criteriaBuilder.equal(root.get(User_.password), password)
        ));

        return this
            .sessionFactory
            .withSession(session -> session.createQuery(query).getSingleResult())
            .convert()
            .with(UniReactorConverters.toMono());

    }

}
