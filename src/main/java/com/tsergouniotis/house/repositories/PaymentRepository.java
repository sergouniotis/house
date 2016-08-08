package com.tsergouniotis.house.repositories;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.tsergouniotis.house.entities.Payment;

@ApplicationScoped
@Transactional
public class PaymentRepository extends GenericRepositoryImpl<Payment, Long> {

	public PaymentRepository() {
		super(Payment.class);
	}

	public Double findSum() {
		TypedQuery<Double> q = em.createNamedQuery("Payment.findSum", Double.class);
		return q.getSingleResult();
	}

}
