package com.rtravez.msc.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.EntityManager;
import java.util.Objects;

@NoRepositoryBean
public abstract class GenericRepository<T, K> extends SimpleJpaRepository<T, K>
        implements IGenericRepository<T, K> {

    protected final EntityManager em;
    protected final JPAQueryFactory queryFactory;
    protected final Class<T> domainType;

    public EntityManager getEntityManager() {
        return em;
    }

    protected GenericRepository(Class<T> domainType, EntityManager em) {
        super(domainType, em);
        this.domainType = Objects.requireNonNull(domainType, "domainType must not be null");
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }  
}
