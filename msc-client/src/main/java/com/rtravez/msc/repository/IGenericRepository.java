package com.rtravez.msc.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface IGenericRepository<T, T1> {

	T save(T t);

	T update(T t);

	void delete(T t);

	void deleteById(T1 id);

	Optional<T> findById(T1 id);

	List<T> findAll();
}
