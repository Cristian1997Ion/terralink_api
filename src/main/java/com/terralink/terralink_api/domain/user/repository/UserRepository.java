package com.terralink.terralink_api.domain.user.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import com.terralink.terralink_api.domain.user.entity.User;

import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserRepository {
    
    private final Mutiny.SessionFactory sessionFactory;

    public Mono<List<User>> findAll() {
        CriteriaBuilder criteriaBuilder =  this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        query.from(User.class);

        return this
            .sessionFactory
            .withSession(session -> session.createQuery(query).getResultList())
            .convert()
            .with(UniReactorConverters.toMono());
    }

}
