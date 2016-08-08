package com.tsergouniotis.house.jsf.converters;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "localDateTimeConverter", forClass = ZonedDateTime.class)
public class LocalDateTimeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return LocalDate.parse(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {

		LocalDate dateValue = LocalDate.class.cast(value) ;
		return dateValue.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

}
