package com.tsergouniotis.house.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.repositories.CreditorRepository;

@ViewScoped
@Named("creditorModel")
public class CreditorModel implements Serializable {

	private Creditor selectedCreditor;

	@Inject
	private CreditorRepository creditorRepository;

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

	public void save() {
		creditorRepository.update(selectedCreditor);
		this.selectedCreditor = null;
	}

}
