package com.tsergouniotis.house.validation.constraints;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.tsergouniotis.house.validation.validators.UniquePaymentCodeValidator;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = { UniquePaymentCodeValidator.class })
public @interface UniquePaymentCode {

	String message() default "{com.tsergouniotis.house.validation.constraints.UniquePaymentCode.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}