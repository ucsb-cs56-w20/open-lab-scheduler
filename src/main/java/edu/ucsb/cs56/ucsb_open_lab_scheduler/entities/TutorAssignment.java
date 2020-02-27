package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TutorAssignment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private CourseOffering courseOffering;

    @ManyToOne
    private Tutor tutor;

    public CourseOffering getCourseOffering() {
        return this.courseOffering;
    }

    public void setCourseOffering(CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
    }

    public Tutor getTutor() {
        return this.tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public void setId(long id){
        this.id = id;
    }    

    public long getId(){
        return id;
    }

    public TutorAssignment(){}

    public TutorAssignment(Tutor tutor, CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
        this.tutor = tutor;
    }
}