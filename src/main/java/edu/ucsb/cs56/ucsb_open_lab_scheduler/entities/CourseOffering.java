package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import com.opencsv.bean.CsvBindByPosition;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Objects;


@Entity
public class CourseOffering implements Comparable<CourseOffering>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CsvBindByPosition(position = 0)
    @NotBlank(message = "Course Id is required")
    private String courseId;

    @CsvBindByPosition(position = 1)
    @NotBlank(message = "Quarter is required")
    private String quarter;

    @CsvBindByPosition(position = 2)
    @NotBlank(message = "Instructor Name is required")
    private String instructorName;

    @CsvBindByPosition(position = 3)
    @NotBlank(message = "Instructor Email is required")
    private String instructorEmail;

    public CourseOffering(){}
    public CourseOffering(long id, String courseId, String quarter, String instructorName, String instructorEmail){
        this.id = id;
        this.courseId = courseId;
        this.quarter = quarter;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
    }

    public void setId(long id){
        this.id = id;
    }  

    public void setCourseId(String courseId){
        this.courseId = courseId;
    }   

    public void setQuarter(String quarter){
        this.quarter = quarter;
    }

    public void setInstructorName(String instructorName){
        this.instructorName = instructorName;
    }

    public void setInstructorEmail(String instructorEmail){
        this.instructorEmail = instructorEmail;
    }

    public long getId(){
        return id;
    }

    public String getCourseId(){
        return courseId;
    } 

    public String getQuarter(){
        return quarter;
    }

    public String getInstructorName(){
        return instructorName;
    }

    public String getInstructorEmail(){
        return instructorEmail;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + id + "'" +
            ", courseId='" + courseId + "'" +
            ", quarter='" + quarter + "'" +
            ", instructorName='" + instructorName + "'" +
            ", instructorEmail='" + instructorEmail + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof CourseOffering)) {
            return false;
        }
        CourseOffering courseOffering = (CourseOffering) o;
        return id == courseOffering.id 
            && Objects.equals(courseId, courseOffering.courseId) 
            && Objects.equals(quarter, courseOffering.quarter) 
            && Objects.equals(instructorName, courseOffering.instructorName) 
            && Objects.equals(instructorEmail, courseOffering.instructorEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quarter, instructorName, instructorEmail);
    }

    @Override
    public int compareTo(CourseOffering otherCourse){
	    return this.quarter.substring(0,1).compareTo(otherCourse.quarter.substring(0,1));
    }

}
