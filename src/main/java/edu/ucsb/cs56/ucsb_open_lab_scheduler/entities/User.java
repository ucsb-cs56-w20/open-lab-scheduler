package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long uid;

    private String ucsbEmail;

    private String firstName;

    private String lastName;

    public User(){}

    public void setId(long id){
        this.id = id;
    }    

    public void setUid(long uid){
        this.uid = uid;
    }

    public void setUcsbEmail(String ucsbEmail){
        this.ucsbEmail = ucsbEmail;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public long getId(){
        return id;
    }

    public long getUid(){
        return uid;
    }

    public String getUcsbEmail(){
        return ucsbEmail;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }

}