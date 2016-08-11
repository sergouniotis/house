package com.tsergouniotis.house.jsf.converters;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import com.tsergouniotis.house.entities.Creditor;
import com.tsergouniotis.house.repositories.CreditorRepository;

//@FacesConverter(value = "creditorConverter", forClass = Creditor.class)
@Named("creditorConverter")
@ApplicationScoped
public class CreditorConverter implements Converter {

	@Inject
	private CreditorRepository creditorRepository;

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (null == value) {
			return null;
		}

		return creditorRepository.findByName(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (null == value) {
			return null;
		}
		return String.valueOf(Creditor.class.cast(value));
	}

}
