package edu.ucsb.cs56.ucsb_open_lab_scheduler.validations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurationValidator implements
  ConstraintValidator<DurationConstraint, Object> {

    public DurationValidator() {}

    private static Logger logger = LoggerFactory.getLogger(DurationValidator.class);

    private String startTime;
    private String endTime;

    
    @Value("${app.timeSlotDefaultDuration}")
    private int defaultDuration;
    

    @Override
    public void initialize(DurationConstraint dur) {
      this.startTime = dur.startTime();
      this.endTime = dur.endTime();
    }
 
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object endTime = new BeanWrapperImpl(value).getPropertyValue("endTime");
        Object startTime = new BeanWrapperImpl(value).getPropertyValue("startTime");
        logger.info((int)endTime + " " + (int)startTime);
        logger.info((int)defaultDuration + "");
        return (militaryToMinutes((int)endTime) - militaryToMinutes((int)startTime)) % (int)defaultDuration == 0;

    }

    public int militaryToMinutes(int time) {
      logger.info(((time / 100) * 60) + (time % 100) + "");
      return ((time / 100) * 60) + (time % 100);
  }
 
}