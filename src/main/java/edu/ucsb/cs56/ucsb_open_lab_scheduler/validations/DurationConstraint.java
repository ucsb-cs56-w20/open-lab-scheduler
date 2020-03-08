package edu.ucsb.cs56.ucsb_open_lab_scheduler.validations;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;


@Documented
@Constraint(validatedBy = DurationValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DurationConstraint {
    String message() default "Invalid duration";
    String startTime();
    String endTime();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {}; 
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        DurationConstraint[] value();
    }
 
}