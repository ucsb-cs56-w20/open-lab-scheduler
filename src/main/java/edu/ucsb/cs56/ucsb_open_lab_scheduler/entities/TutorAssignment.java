package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class TutorAssignment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long courseOfferingId;

    private long tutorId;

    public TutorAssignment(){}

    public TutorAssignment(Tutor tutor, CourseOffering courseOffering){
        this.tutorId = tutor.getId();
        this.courseOfferingId = courseOffering.getId();
    }

    public void setId(long id){
        this.id = id;
    }    

    public void setCourseOfferingId(long courseOfferingId){
        this.courseOfferingId = courseOfferingId;
    }

    public void setTutorId(long tutorId){
        this.tutorId = tutorId;
    }

    public long getId(){
        return id;
    }

    public long getCourseOfferingId(){
        return courseOfferingId;
    }

    public long getTutorId(){
        return tutorId;
    }

}