package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

@Entity
public class RoomAvailability{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Quarter is required")
    private String quarter;

    @NotBlank(message = "Start time is required")
    private int startTime;

    @NotBlank(message = "End time is required")
    private int endTime;

    @NotBlank(message = "Day is required")
    private String day;

    @OneToOne
    @JoinColumn(name = "room_id")
    @NotBlank(message = "Room is required")
    private Room room;

    public RoomAvailability(long id, String quarter, int startTime, int endTime, String day, Room room) {
        this.id = id;
        this.quarter = quarter;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.room = room;
    }

    public RoomAvailability(){}
    
    public String getQuarter() {
        return this.quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
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
    public String toString() {
        return "{" +
            " id='" + id + "'" +
            ", quarter='" + quarter + "'" +
            ", startTime='" + getTime12HrFormat(startTime) + "'" +
            ", endTime='" + getTime12HrFormat(endTime) + "'" +
            ", day='" + day + "'" +
            ", room='" + room + "'" +
            "}";
    }
   
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof RoomAvailability)) {
            return false;
        }
        RoomAvailability roomAvailability = (RoomAvailability) o;
        return id == roomAvailability.id && Objects.equals(quarter, roomAvailability.quarter) && startTime == roomAvailability.startTime && endTime == roomAvailability.endTime && Objects.equals(day, roomAvailability.day) && Objects.equals(room, roomAvailability.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quarter, startTime, endTime, day, room);
    }
}