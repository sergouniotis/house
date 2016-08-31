package com.tsergouniotis.house.validation.validators;

import java.util.Objects;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.tsergouniotis.house.repositories.PaymentRepository;
import com.tsergouniotis.house.validation.constraints.UniquePaymentCode;

public class UniquePaymentCodeValidator implements ConstraintValidator<UniquePaymentCode, String> {

	@Inject
	private PaymentRepository paymentRepository;

	@Override
	public void initialize(UniquePaymentCode constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (Objects.isNull(value) || value.length() < 1) {
			return false;
		}

		Long id = paymentRepository.findByPaymentCode(value);
		if (Objects.isNull(id)) {
			return true;
		}

		return false;
	}

}
