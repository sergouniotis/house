package com.tsergouniotis.house.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.repositories.CreditorRepository;
import com.tsergouniotis.house.repositories.PaymentRepository;

@ViewScoped
@Named("paymentModel")
// @ManagedBean(name = "paymentModel")
public class PaymentModel implements Serializable {

	@Inject
	private PaymentRepository paymentRepository;

	@Inject
	private CreditorRepository creditorRepository;

	private Payment selectedPayment;

	public List<Payment> getPayments() {
		return paymentRepository.findAll();
	}

	public void selectPayment(Payment payment) {
		this.selectedPayment = payment;
	}

	public void setSelectedPayment(Payment selectedPayment) {
		this.selectedPayment = selectedPayment;
	}

	public Payment getSelectedPayment() {
		if (Objects.isNull(selectedPayment)) {
			this.selectedPayment = new Payment();
		}
		return selectedPayment;
	}

	public void save() {		
		paymentRepository.update(selectedPayment);
		selectedPayment = null;

	}

	public void creditorChange(ActionEvent e) {
		this.selectedPayment.setCreditor(null);
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
