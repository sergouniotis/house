package com.tsergouniotis.house.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.repositories.PaymentRepository;

//@ViewScoped
//@Named("paymentModel")
@ManagedBean(name = "paymentModel")
public class PaymentModel implements Serializable {

	@EJB
	private PaymentRepository paymentRepository;

	private Payment newPayment;

	public List<Payment> getPayments() {

		return paymentRepository.findAll();
	}

	public Payment getNewPayment() {
		if (Objects.isNull(newPayment)) {
			this.newPayment = new Payment();
		}
		return newPayment;
	}

	public void savePayment(ActionEvent actionEvent) {
		paymentRepository.save(newPayment);
	}

	public void addMessage(String summary) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, null);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
