package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class TimeSlot{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long roomAvailabilityId;
    private int startTime;

    public TimeSlot(){}

    public void setId(long id){
        this.id = id;
    }    

    public void setRoomAvailabilityId(long roomAvailabilityId){
        this.roomAvailabilityId = roomAvailabilityId;
    }

    public void setStartTime(int startTime){
        this.startTime = startTime;
    }

    public long getId(){
        return id;
    }

    public long getRoomAvailabilityId(){
        return roomAvailabilityId;
    }

    public int getStartTime(){
        return startTime;
    }

}