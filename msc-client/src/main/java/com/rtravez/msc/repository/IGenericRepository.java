package com.rtravez.msc.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface IGenericRepository<T, K> {

	T save(T t);

	T update(T t);

	void delete(T t);

	void deleteById(K id);

	Optional<T> findById(K id);

	List<T> findAll();
}
