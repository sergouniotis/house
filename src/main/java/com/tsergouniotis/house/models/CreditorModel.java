package com.tsergouniotis.house.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.entities.CreditorType;
import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.repositories.CreditorRepository;
import com.tsergouniotis.house.repositories.PaymentRepository;

@ViewScoped
@Named("creditorModel")
public class CreditorModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7220592461809012044L;

	private Creditor selectedCreditor;

	@Inject
	private CreditorRepository creditorRepository;

	@Inject
	private PaymentRepository paymentRepository;

	public Creditor getSelectedCreditor() {
		if (Objects.isNull(selectedCreditor)) {
			this.selectedCreditor = new Creditor();
		}
		return selectedCreditor;
	}

	public void setSelectedCreditor(Creditor selectedCreditor) {
		this.selectedCreditor = selectedCreditor;
	}

	public List<Creditor> getCreditors() {
		List<Creditor> result = creditorRepository.findAll();
		if (Objects.isNull(result)) {
			result = new ArrayList<>();
		}
		return result;
	}

	public List<CreditorType> getCreditorTypes() {
		return Arrays.asList(CreditorType.values());
	}

	public void save() {
		creditorRepository.saveOrUpdate(selectedCreditor);
		this.selectedCreditor = null;
	}

	public void delete(Creditor creditor) {
		try {

			boolean hasPayments = hasPayments(creditor);
			if (hasPayments) {
				throw new Exception("Creditor has payments assigned  and  thus cannot be deleted.");
			}
			creditorRepository.delete(creditor);
		} catch (Exception e) {
			FacesUtils.error(e.getMessage());
		}
	}

	private boolean hasPayments(Creditor creditor) {
		List<Payment> payments = paymentRepository.findByCreditor(creditor);
		return null != payments && payments.size() > 0;
	}
}
