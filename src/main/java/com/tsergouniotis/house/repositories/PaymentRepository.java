package com.tsergouniotis.house.repositories;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.tsergouniotis.house.entities.Payment;

@Stateless
public class PaymentRepository extends GenericRepositoryImpl<Payment, Long> {

	public PaymentRepository() {
		super(Payment.class);
	}

}
