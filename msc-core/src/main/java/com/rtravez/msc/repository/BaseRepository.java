package com.rtravez.msc.repository;

import jakarta.persistence.EntityManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public abstract class BaseRepository<T, ID> extends SimpleJpaRepository<T, ID>
        implements IBaseRepository<T, ID> {
    protected final JPAQueryFactory queryFactory;

    protected BaseRepository(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}