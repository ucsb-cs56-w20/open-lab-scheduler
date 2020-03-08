package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class TutorAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "course_offering_id")
    private CourseOffering courseOffering;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;
    
    private boolean isCourseLead;

    @NotBlank(message = "Type is required")
    private String type;

    public TutorAssignment(){}

    public TutorAssignment(Tutor tutor, CourseOffering courseOffering, boolean lead){
        this.tutor = tutor;
        this.courseOffering = courseOffering;
        this.isCourseLead = lead;
    }

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
    
     public void setIsCourseLead(boolean isCourseLead){
         this.isCourseLead = isCourseLead;
     }

     public boolean getIsCourseLead(){
         return isCourseLead;
     }

    public String getType() {
        return type;
    }

}
