package com.tsergouniotis.house.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment implements Serializable {

	@Id
	@Column(name = "database_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENTSEQ")
	@SequenceGenerator(name = "PAYMENTSEQ", sequenceName = "payment_seq", allocationSize = 1)
	private Long id;

	@Column(name = "description")
	private String description;

	@Column(name = "amount")
	private Double amount;

	@Column(name = "payment_day")
	private LocalDate paymentDay;

	@ManyToOne
	@JoinColumn(name = "creditor_sid", referencedColumnName = "database_id")
	private Creditor creditor;

	public Long getId() {
		return id;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDate getPaymentDay() {
		return paymentDay;
	}

	public void setPaymentDay(LocalDate paymentDay) {
		this.paymentDay = paymentDay;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Creditor getCreditor() {
		return creditor;
	}

	public void setCreditor(Creditor creditor) {
		this.creditor = creditor;
	}

}
