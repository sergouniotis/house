package com.tsergouniotis.house.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.tsergouniotis.house.validation.constraints.UniquePaymentCode;

@Entity
@Table(name = "payments")
@NamedQueries({ @NamedQuery(name = "Payment.findSum", query = "SELECT SUM(p.amount) FROM Payment p"),
		@NamedQuery(name = "Payment.findPerCreditor", query = "SELECT p FROM Payment p where p.creditor=:creditor"),
		@NamedQuery(name = "Payment.findSumPerCreditor", query = "SELECT SUM(p.amount) FROM Payment p where p.creditor=:creditor"),
		@NamedQuery(name = "Payment.findByPaymentCode", query = "SELECT p.id FROM Payment p where p.code=:code") })
public class Payment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "database_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENTSEQ")
	@SequenceGenerator(name = "PAYMENTSEQ", sequenceName = "payment_seq", allocationSize = 1)
	private Long id;

	@Column(name = "description")
	private String description;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "payment_day")
	private LocalDate paymentDay;

	@ManyToOne
	@JoinColumn(name = "creditor_sid", referencedColumnName = "database_id")
	private Creditor creditor;

	@Column(name = "is_extra")
	private boolean extra;

	@UniquePaymentCode
	@Column(name = "payment_code", unique = true)
	private String code;

	@Embedded
	private PFile file;

	public Long getId() {
		return id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
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

	public PFile getFile() {
		return file;
	}

	public void setFile(PFile file) {
		this.file = file;
	}

	public boolean isExtra() {
		return extra;
	}

	public void setExtra(boolean extra) {
		this.extra = extra;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
