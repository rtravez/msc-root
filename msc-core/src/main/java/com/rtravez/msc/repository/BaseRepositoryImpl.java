package com.rtravez.msc.repository;

import jakarta.persistence.EntityManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public abstract class BaseRepositoryImpl<T, I> extends SimpleJpaRepository<T, I>
        implements BaseRepository<T, I> {
    protected final JPAQueryFactory queryFactory;

    protected BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}