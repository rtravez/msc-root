package com.rtravez.msc.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import org.springframework.data.repository.NoRepositoryBean;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@NoRepositoryBean
public abstract class GenericRepository<T, K> implements IGenericRepository<T, K> {

	protected EntityManager em;
	protected JPAQueryFactory queryFactory;
	protected final Class<T> domainType;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager em) {
		this.em = em;
		this.queryFactory = new JPAQueryFactory(em);
	}

	protected GenericRepository(Class<T> domainType) {
		this.domainType = Objects.requireNonNull(domainType, "domainType must not be null");
	}

	@Override
	public T save(T t) {
		em.persist(t);
		return t;
	}

	@Override
	public void delete(T t) {
		em.remove(em.merge(t));
	}

	@Override
	public T update(T t) {
		return em.merge(t);
	}

	@Override
	public List<T> findAll() {
		EntityPath<T> entityPath = new EntityPathBase<>(domainType, domainType.getSimpleName());
		return queryFactory.selectFrom(entityPath).fetch();
	}

	@Override
	public Optional<T> findById(K id) {
		return Optional.ofNullable(em.find(domainType, id));
	}

	@Override
	public void deleteById(K id) {
		this.findById(id).ifPresent(this::delete);
	}
}
