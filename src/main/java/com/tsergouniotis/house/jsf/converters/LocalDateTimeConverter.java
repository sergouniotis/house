package com.tsergouniotis.house.jsf.converters;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.tsergouniotis.house.models.FacesUtils;

@FacesConverter(value = "localDateTimeConverter", forClass = ZonedDateTime.class)
public class LocalDateTimeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		try {
			return LocalDate.parse(value);
		} catch (Exception e) {
			String msg = "Could not convert value " + value + " to a valid " + LocalDate.class + " class";
			throw new ConverterException(FacesUtils.newFacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		if (Objects.isNull(value)) {
			return null;
		}

		LocalDate dateValue = LocalDate.class.cast(value);
		return dateValue.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

}
