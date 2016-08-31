package com.tsergouniotis.house.repositories;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.entities.Payment;

@ApplicationScoped
@Transactional
public class PaymentRepository extends GenericRepositoryImpl<Payment, Long> {

	public PaymentRepository() {
		super(Payment.class);
	}

	public BigDecimal findSum() {
		TypedQuery<BigDecimal> q = em.createNamedQuery("Payment.findSum", BigDecimal.class);
		return q.getSingleResult();
	}

	public BigDecimal findSumPerCreditor(Creditor creditor) {
		TypedQuery<BigDecimal> q = em.createNamedQuery("Payment.findSumPerCreditor", BigDecimal.class);
		q.setParameter("creditor", creditor);
		return q.getSingleResult();
	}

	public List<Payment> findByCreditor(Creditor creditor) {
		TypedQuery<Payment> q = em.createNamedQuery("Payment.findPerCreditor", Payment.class);
		q.setParameter("creditor", creditor);
		return q.getResultList();
	}

	public Long findByPaymentCode(String code) {
		try {
			TypedQuery<Long> q = em.createNamedQuery("Payment.findByPaymentCode", Long.class);
			q.setParameter("code", code);
			return q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
