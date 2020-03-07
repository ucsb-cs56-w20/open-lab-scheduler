package edu.ucsb.cs56.ucsb_open_lab_scheduler.formbeans;

public class CourseSchedule{
    private String courseId;
    private String quarter;

    public CourseSchedule(){
        this.courseId = "";
        this.quarter = "";
    }

    public void setCourseId(String c){
        courseId = c;
    }

    public String getCourseId(){
        return courseId;
    }

    public void setQuarter(String q){
        quarter = q;
    }

    public String getQuarter(){
        return quarter;
    }
}
