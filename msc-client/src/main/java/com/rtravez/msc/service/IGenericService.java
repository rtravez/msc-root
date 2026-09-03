package com.rtravez.msc.service;

import com.rtravez.msc.exception.ExceptionManager;

import java.util.List;
import java.util.Optional;

public interface IGenericService<T, T1> {

	T save(T entity) throws ExceptionManager;

	T update(T entity) throws ExceptionManager;

	Optional<T> findById(T1 id) throws ExceptionManager;

	List<T> findAll() throws ExceptionManager;

	void deleteById(T1 id) throws ExceptionManager;

	void delete(T entity) throws ExceptionManager;
}
