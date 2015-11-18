package com.caferaters.repository;

import java.io.Serializable;

public interface CrudRepository<T, ID extends Serializable> {
	ID save(T entity);
	void delete(T entity);
	void update(T entity);
	T findById(ID id);
}
