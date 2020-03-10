package edu.ucsb.cs56.ucsb_open_lab_scheduler.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = TimeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeConstraint {
    String message() default "Invalid time format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}