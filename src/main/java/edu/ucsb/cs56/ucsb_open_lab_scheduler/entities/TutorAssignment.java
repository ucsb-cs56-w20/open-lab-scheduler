package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class TutorAssignment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_offering_id")
    private CourseOffering courseOffering;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @NotBlank(message = "Type is required")
    private String type;

    public TutorAssignment(){}

    public TutorAssignment(Tutor tutor, CourseOffering courseOffering, String type){
        this.tutor = tutor;
        this.courseOffering = courseOffering;
        this.type = type;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setCourseOffering(CourseOffering courseOffering){
        this.courseOffering = courseOffering;
    }

    public void setTutor(Tutor tutor){
        this.tutor = tutor;
    }

    public void setType(String type)   {
        this.type = type;
    }

    public long getId(){
        return id;
    }

    public CourseOffering getCourseOffering(){
        return courseOffering;
    }

    public Tutor getTutor(){
        return tutor;
    }

    public String getType() {
        return type;
    }

}
