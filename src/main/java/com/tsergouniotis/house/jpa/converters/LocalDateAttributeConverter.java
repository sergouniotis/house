package com.tsergouniotis.house.jpa.converters;

import static java.time.ZoneId.systemDefault;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate value) {
		return value == null ? null : Date.from(value.atStartOfDay(systemDefault()).toInstant());
	}

	@Override
	public LocalDate convertToEntityAttribute(Date value) {
		return value == null ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), systemDefault()).toLocalDate();
	}
}
