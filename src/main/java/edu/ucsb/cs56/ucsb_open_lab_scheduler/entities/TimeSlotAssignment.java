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

    public TimeSlotAssignment(TimeSlot timeSlot, Tutor tutor, CourseOffering courseOffering) {
        this.timeSlot = timeSlot;
        this.tutor = tutor;
        this.courseOffering = courseOffering;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setTimeSlot(TimeSlot timeSlot){
        this.timeSlot = timeSlot;
    }

    public void setTutor(Tutor tutor){
        this.tutor = tutor;
    }

    public void setCourseOffering(CourseOffering courseOffering){
        this.courseOffering = courseOffering;
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

<<<<<<< HEAD
    public CourseOffering getCourseOffering(){
=======
    public CourseOffering getCourseOffering() {
>>>>>>> bq - implemented drop down menu for list of tutors sorted by their course then by last name
        return courseOffering;
    }

}
