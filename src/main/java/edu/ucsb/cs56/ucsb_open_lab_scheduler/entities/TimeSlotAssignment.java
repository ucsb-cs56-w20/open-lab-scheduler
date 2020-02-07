package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class TimeSlotAssignment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long timeSlotId;

    private long tutorId;

    public TimeSlotAssignment(){}

    public void setId(long id){
        this.id = id;
    }    

    public void setTimeSlotId(long timeSlotId){
        this.timeSlotId = timeSlotId;
    }

    public void setTutorId(long tutorId){
        this.tutorId = tutorId;
    }

    public long getId(){
        return id;
    }

    public long getTimeSlotId(){
        return timeSlotId;
    }

    public long getTutorId(){
        return tutorId;
    }

}