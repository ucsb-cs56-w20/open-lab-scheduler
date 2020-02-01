package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
public class TimeSlot{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String quarter;//QYY
    private long roomId;
    private int startTime;
    private int endTime;
    private String day;

    public TimeSlot(String quarter, String room, int startTime, int endTime, String day){
        setQuarter(quarter);
        setRoomId(room);
        setStartTime(startTime);
        setEndTime(endTime);
        setDay(day);
    }

    public long getId(){
        return id;
    }
    public void setId(long id){
        this.id = id;
    }

    public String getQuarter(){
        return quarter;
    }  
    public void setQuarter(String quarter){
        this.quarter = quarter;
    }

    public long getRoomId(){
        return roomId;
    }
    public void setRoomId(long roomId){
        this.roomId = roomId;
    }
    public void setRoomId(String roomName){
        //stub
    }
    public String getRoom(){
        return "stub";
    }

    public int getStartTime(){
        return startTime;
    }
    public void setStartTime(int StartTime){
        this.startTime = startTime;
    }

    public int getEndTime(){
        return endTime;
    }
    public void setEndTime(int endTime){
        this.endTime = endTime;
    }

    public String getDay(){
        return day;
    }
    public void setDay(String day){
        this.day = day;
    }


    @OneToOne
    @JoinColumn(name = "room_id")
    @NotBlank(message = "Room is required")
    private Room room;

    public TimeSlot(){}
    
    public TimeSlot(int startTime, int endTime, String day, Room room){
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.room = room;
    }

    public int getStartTime(){
        return startTime;
    }

    public int getEndTime(){
        return endTime;
    }

    public String getDay(){
        return day;
    }

    public Room getRoom(){
        return room;
    }

    public void setStartTime(int startTime){
        this.startTime = startTime;
    }

    public void setEndTime(int endTime){
        this.endTime = endTime;
    }

    public void setDay(String day){
        this.day = day;
    }

    public void setRoom(Room room){
        this.room = room;
    }

    public String getTime12HrFormat(int t){
        String time = t <= 1200 ? Integer.toString(t) : Integer.toString(t%1200);
        String suffix = t < 1200 ? "am" : "pm";
        if (time.length()==2)
        { time = "12" + time;}
        return time.substring(0,time.length()-2)+":"+time.substring(time.length()-2)+ " "+ suffix;
    }

    @Override
    public String toString(){
        return day+" ["+startTime+" - "+endTime+"] "+room.toString();
    }
}
