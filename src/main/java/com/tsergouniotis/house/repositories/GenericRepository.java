package com.tsergouniotis.house.repositories;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface GenericRepository<T, PK extends Serializable> {

	T find(PK id);

	T save(T t);

	void saveAll(Collection<T> all);

	T update(T t);

	void delete(T t);

	void deleteAll();

	List<T> findAll();
}
