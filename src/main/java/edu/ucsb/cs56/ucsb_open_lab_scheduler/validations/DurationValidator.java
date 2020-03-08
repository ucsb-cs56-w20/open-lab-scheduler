package edu.ucsb.cs56.ucsb_open_lab_scheduler.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

public class DurationValidator implements
  ConstraintValidator<DurationConstraint, Integer> {

    @Value("${app.timeSlotDefaultDuration}")
    private int defaultDuration;
 
    @Override
    public void initialize(DurationConstraint dur) {
    }

    @
 
    @Override
    public boolean isValid(Integer dur,
      ConstraintValidatorContext cxt) {
        return dur % defaultDuration == 0;

    }
 
}