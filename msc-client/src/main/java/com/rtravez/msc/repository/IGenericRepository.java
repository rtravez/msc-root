package com.sofka.msc.repository;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface IGenericRepository<T, ID extends Serializable> {

	T save(T t);

	T update(T t);

	void delete(T t);

	void deleteById(ID id);

	Optional<T> findById(ID id);

	List<T> findAll();
}
