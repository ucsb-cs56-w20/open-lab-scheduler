package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

@Entity
public class TutorCheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private long timeSlotAssignmentId;

    @NotBlank
    private String date;

    @NotBlank
    private String remarks;

    public TutorCheckIn() {
    }

    public TutorCheckIn(long timeSlotAssignmentId) {
        this.timeSlotAssignmentId = timeSlotAssignmentId;
    }

    public TutorCheckIn(long timeSlotAssignmentId, String date, String remarks) {
        this.timeSlotAssignmentId = timeSlotAssignmentId;
        this.date = date;
        this.remarks = remarks;
    }

    public void setId(long timeSlotAssignmentId) {
        this.timeSlotAssignmentId = timeSlotAssignmentId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getId() {
        return timeSlotAssignmentId;
    }

    public String getDate() {
        return date;
    }

    public String getRemarks() {
        return remarks;
    }

}