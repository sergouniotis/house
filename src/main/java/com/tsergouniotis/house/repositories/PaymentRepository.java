package com.tsergouniotis.house.repositories;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.utils.CollectionUtils;
import com.tsergouniotis.house.utils.MapUtils;

@ApplicationScoped
@Transactional
public class PaymentRepository extends GenericRepositoryImpl<Payment, Long> {

	public PaymentRepository() {
		super(Payment.class);
	}

	public Long count() {
		TypedQuery<Long> q = em.createQuery("select count(p.id) from Payment p", Long.class);
		return q.getSingleResult();
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

	public List<Payment> find(int first, int pageSize, Map<String, Boolean> orderMap, Map<String, Object> filters) {

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Payment> criteria = builder.createQuery(Payment.class);

		Root<Payment> from = criteria.from(Payment.class);

		List<Predicate> predicates = new ArrayList<>();
		if (null != filters && !filters.isEmpty()) {
			for (String property : filters.keySet()) {
				Object value = filters.get(property);

				String[] properties = property.split("\\.");
				Path<Object> x = from.get(properties[0]);
				for (int i = 1; i < properties.length; i++) {
					x = x.get(properties[i]);
				}

				Predicate predicate = builder.like(x.as(String.class), value.toString().concat("%"));
				predicates.add(predicate);
			}
		}

		List<Order> orders = new ArrayList<>();
		if (!MapUtils.isEmpty(orderMap)) {
			for (String sortField : orderMap.keySet()) {

				Path<Object> sortingAttribute = from.get(sortField);

				Boolean ascending = orderMap.get(sortField);
				if (Objects.isNull(ascending)) {
					continue;
				} else if (ascending) {
					orders.add(builder.asc(sortingAttribute));
				} else {
					orders.add(builder.desc(sortingAttribute));
				}
			}
		}

		Predicate[] constraints = predicates.toArray(new Predicate[predicates.size()]);

		CriteriaQuery<Payment> select = criteria.select(from).where(constraints);

		if (CollectionUtils.isNotEmpty(orders)) {
			select.orderBy(orders);
		}

		TypedQuery<Payment> typedQuery = em.createQuery(select);
		typedQuery.setFirstResult(first);
		typedQuery.setMaxResults(pageSize);

		return typedQuery.getResultList();

	}

	public Long count(Map<String, Object> filters) {

		CriteriaBuilder builder = em.getCriteriaBuilder();

		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);

		Root<Payment> from = criteria.from(Payment.class);

		List<Predicate> predicates = new ArrayList<>();
		if (null != filters && !filters.isEmpty()) {
			for (String property : filters.keySet()) {
				Object value = filters.get(property);

				String[] properties = property.split("\\.");
				Path<Object> x = from.get(properties[0]);
				for (int i = 1; i < properties.length; i++) {
					x = x.get(properties[i]);
				}

				Predicate predicate = builder.like(x.as(String.class), value.toString().concat("%"));
				predicates.add(predicate);
			}
		}

		Predicate[] constraints = predicates.toArray(new Predicate[predicates.size()]);

		CriteriaQuery<Long> select = criteria.select(builder.count(from)).where(constraints);

		TypedQuery<Long> typedQuery = em.createQuery(select);

		return typedQuery.getSingleResult();

	}

}
