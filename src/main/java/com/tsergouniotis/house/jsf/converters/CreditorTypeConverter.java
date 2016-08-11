/**
 * 
 */
package com.tsergouniotis.house.jsf.converters;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tsergouniotis.house.entities.CreditorType;

/**
 * @author sergouniotis
 *
 */
@Named("creditorTypeConverter")
@ApplicationScoped
public class CreditorTypeConverter implements Converter {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreditorTypeConverter.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.
	 * FacesContext, javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (Objects.isNull(value) || value.length() < 1) {
			return null;
		}

		try {
			return CreditorType.valueOf(value);
		} catch (Exception e) {
			String msg = "Cannot convert creditor type value " + value;
			LOGGER.warn(msg, e);
			throw new ConverterException(msg);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.
	 * FacesContext, javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (Objects.isNull(value)) {
			return null;
		}

		return CreditorType.class.cast(value).name();

	}

}
