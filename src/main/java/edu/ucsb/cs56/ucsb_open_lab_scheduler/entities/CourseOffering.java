package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class CourseOffering{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String className;
    private String quarter;
    private long instructorId;

    public CourseOffering(){}

    public void setId(long id){
        this.id = id;
    }    

    public void setClass(String className){
        this.className = className;
    }

    public void setQuarter(String quarter){
        this.quarter = quarter;
    }

    public void setInstructorId(long instructorId){
        this.instructorId = instructorId;
    }

    public long getId(){
        return id;
    }

    public String getClassName(){
        return className;
    }

    public String getQuarter(){
        return quarter;
    }

    public long getInstructorId(){
        return instructorId;
    }

}