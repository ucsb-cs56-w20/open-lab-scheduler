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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    public TimeSlotAssignment(){}

    public void setId(long id){
        this.id = id;
    }

    public void setTimeSlot(TimeSlot timeSlot){
        this.timeSlot = timeSlot;
    }

    public void setTutor(Tutor tutor){
        this.tutor = tutor;
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
