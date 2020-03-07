package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.*;

@Entity
public class TimeSlotAssignment{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "time_slot_id")
    private TimeSlot timeSlot;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "course_offering_id")
    private CourseOffering courseOffering;

    public TimeSlotAssignment(){}

    public void setId(long id){
        this.id = id;
    }

    public void setCourseOffering(CourseOffering courseOffering){
        this.courseOffering = courseOffering;
    }

    public void setTimeSlot(TimeSlot timeSlot){
        this.timeSlot = timeSlot;
    }

    public void setTutor(Tutor tutor){
        this.tutor = tutor;
    }

    public CourseOffering getCourseOffering(){
        return courseOffering;
    }

    public long getId(){
        return id;
    }

    public TimeSlot getTimeSlot(){
        return timeSlot;
    }

    public Tutor getTutor(){
        return tutor;
    }

}
