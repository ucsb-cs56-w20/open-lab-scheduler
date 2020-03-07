package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class RoomAvailability{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CsvBindByPosition(position = 0)
    @NotBlank(message = "Quarter is required")
    private String quarter;

    @CsvBindByPosition(position = 2)
    @NotNull
    private int startTime;

    @CsvBindByPosition(position = 3)
    @NotNull
    private int endTime;

    @CsvBindByPosition(position = 1)
    @NotBlank(message = "Day is required")
    private String day;

    @CsvRecurse
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "room_id")
    private Room room;

    public RoomAvailability(long id, String quarter, int startTime, int endTime, String day, Room room) {
        this.id = id;
        this.quarter = quarter;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.room = room;
    }

    public RoomAvailability(String quarter, int startTime, int endTime, String day, Room room) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        String checkForSixty = Integer.toString(t);
        checkForSixty = checkForSixty.substring(checkForSixty.length() - 2);
        String check = "60";
        if(check.equals(checkForSixty)) t += 40;
        String time = t <= 1200 ? Integer.toString(t) : Integer.toString(t%1200);
        String suffix = t < 1200 ? "am" : "pm";
        if (time.length()==2)
        { time = "12" + time;}
        else if (time.length()==1) {
            time = "120" + time;
        }
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
