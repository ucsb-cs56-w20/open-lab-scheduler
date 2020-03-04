package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.*;

@Entity
public class TutorAssignment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "course_offering_id")
    private CourseOffering courseOffering;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    public TutorAssignment(){}

    public TutorAssignment(Tutor tutor, CourseOffering courseOffering){
        this.tutor = tutor;
        this.courseOffering = courseOffering;
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

    public long getId(){
        return id;
    }

    public CourseOffering getCourseOffering(){
        return courseOffering;
    }

    public Tutor getTutor(){
        return tutor;
    }

}
