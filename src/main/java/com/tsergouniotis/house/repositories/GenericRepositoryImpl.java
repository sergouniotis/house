package com.tsergouniotis.house.repositories;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public abstract class GenericRepositoryImpl<T, PK extends Serializable> implements GenericRepository<T, PK> {

	protected Class<T> entityClass;

	@PersistenceContext
	protected EntityManager em;

	public GenericRepositoryImpl(Class<T> type) {
		this.entityClass = type;
	}

	@Override
	public T save(T t) {
		this.em.persist(t);
		return t;
	}

	@Override
	public void saveAll(Collection<T> all) {
		if (null != all) {
			for (T t : all) {
				save(t);
			}
		}
	}

	@Override
	public T find(PK id) {
		return this.em.find(entityClass, id);
	}

	@Override
	public T saveOrUpdate(T t) {
		return this.em.merge(t);
	}

	@Override
	public void deleteAll() {
		List<T> all = findAll();
		if (null != all) {
			for (T t : all) {
				delete(t);
			}
		}
	}

	@Override
	public List<T> findAll() {

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(entityClass);

		Root<T> root = criteriaQuery.from(entityClass);
		criteriaQuery.select(root);

		return em.createQuery(criteriaQuery).getResultList();

	}

	@Override
	public void delete(T t) {
		t = this.em.merge(t);
		this.em.remove(t);
	}

}
