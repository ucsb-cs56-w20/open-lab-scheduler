package edu.ucsb.cs56.ucsb_open_lab_scheduler.formbeans;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;

import java.io.Serializable;

public class TutorDetailsForm implements Serializable {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    public String title;

    public TutorDetailsForm() {
        title = "Create New Tutor";
    }

    public TutorDetailsForm(Tutor t) {
        System.out.println("tototototo " + t.getId());
        this.firstName = t.getFirstName();
        this.lastName = t.getLastName();
        this.email = t.getEmail();
        this.id = t.getId();
        title = "Update Tutor Details";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void updateTutor(Tutor t) {
        t.setLastName(lastName);
        t.setFirstName(firstName);
        t.setEmail(email);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
