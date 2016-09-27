package com.tsergouniotis.house.validation.validators;

import java.util.Objects;

import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.tsergouniotis.house.entities.Payment;
import com.tsergouniotis.house.repositories.PaymentRepository;
import com.tsergouniotis.house.validation.constraints.ValidPayment;

public class PaymentValidator implements ConstraintValidator<ValidPayment, Payment> {

	@Inject
	private PaymentRepository paymentRepository;

	@Override
	public void initialize(ValidPayment arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid(Payment value, ConstraintValidatorContext context) {
		if (Objects.isNull(value)) {
			return true;
		}

		String code = value.getCode();

		Long id = paymentRepository.findByPaymentCode(code);
		if (!Objects.isNull(id)) {
			return false;
		}

		return true;
	}

}
