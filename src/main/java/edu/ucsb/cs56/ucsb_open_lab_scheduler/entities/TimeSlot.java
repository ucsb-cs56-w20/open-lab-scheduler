package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Id;


@Entity
public class TimeSlot{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "room_availability_id")
    private RoomAvailability roomAvailability;

    private int startTime;
    private int duration;

    public TimeSlot(){
        this.startTime = 0;
        this.duration = 30;
    }

    public void setId(long id){
        this.id = id;
    }

    public void setRoomAvailability(RoomAvailability roomAvailability){
        this.roomAvailability = roomAvailability;
    }

    public void setStartTime(int startTime){
        this.startTime = startTime;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public long getId(){
        return id;
    }

    public RoomAvailability getRoomAvailability(){
        return roomAvailability;
    }

    public int getStartTime(){
        return startTime;
    }

    public int getDuration(){
        return duration;
    }

    public int getEndTime(){
        return duration + startTime;
    }

}
