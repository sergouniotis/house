package com.tsergouniotis.house.repositories;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.tsergouniotis.house.entities.Creditor;

@ApplicationScoped
@Transactional
@Named("creditorRepository")
public class CreditorRepository extends GenericRepositoryImpl<Creditor, Long> {

	public CreditorRepository() {
		super(Creditor.class);
	}

	public Creditor findByName(String name) {
		try {
			TypedQuery<Creditor> q = em.createNamedQuery("Creditor.findByName", entityClass);
			q.setParameter("name", name);
			return q.getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			return null;
		}

	}

}
