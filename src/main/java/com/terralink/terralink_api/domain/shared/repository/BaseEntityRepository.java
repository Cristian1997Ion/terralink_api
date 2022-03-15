package com.terralink.terralink_api.domain.shared.repository;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.hibernate.reactive.mutiny.Mutiny;

import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public abstract class BaseEntityRepository<T> {
    protected final Mutiny.SessionFactory sessionFactory;
    protected final Class<T> entityClass;

    public Mono<T> findByPk(Object pk) {
        Objects.requireNonNull(pk, "id can not be null");
        return this
            .sessionFactory
            .withSession(session -> session.find(entityClass, pk))
            .convert()
            .with(UniReactorConverters.toMono());
    }

    public Mono<List<T>> findAll() {
        CriteriaBuilder criteriaBuilder =  this.sessionFactory.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(entityClass);
        query.from(entityClass);

        return this
            .sessionFactory
            .withSession(session -> session.createQuery(query).getResultList())
            .convert()
            .with(UniReactorConverters.toMono());
    }


    public Mono<T> save(T entity) {
        return this
            .sessionFactory
            .withSession(session ->
                session.persist(entity)
                    .chain(session::flush)
                    .replaceWith(entity)
            )
            .convert()
            .with(UniReactorConverters.toMono());
    }

    public Mono<T> update(T entity) {
        return this
            .sessionFactory
            .withSession(session -> session.merge(entity).onItem().call(session::flush))
            .convert()
            .with(UniReactorConverters.toMono());
    }

    public Mono<Boolean> delete(T entity) {
        return this
            .sessionFactory
            .withSession(session -> 
                session
                    .remove(entity)
                    .chain(session::flush)
                    .onItem().transform(item -> true)
                    .onFailure().recoverWithItem(false)
            )
            .convert()
            .with(UniReactorConverters.toMono());
    }
}
