package com.tsergouniotis.house.repositories;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.tsergouniotis.house.entities.Payment;

@ApplicationScoped
@Transactional
public class PaymentRepository extends GenericRepositoryImpl<Payment, Long> {

	public PaymentRepository() {
		super(Payment.class);
	}

}
