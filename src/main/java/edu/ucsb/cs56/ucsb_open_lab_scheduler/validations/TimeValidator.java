package edu.ucsb.cs56.ucsb_open_lab_scheduler.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TimeValidator implements
  ConstraintValidator<TimeConstraint, Integer> {
 
    @Override
    public void initialize(TimeConstraint time) {
    }
 
    @Override
    public boolean isValid(Integer time,
      ConstraintValidatorContext cxt) {
        return !(time % 100 > 59 || time < 0 || time > 2400); 

    }
 
}