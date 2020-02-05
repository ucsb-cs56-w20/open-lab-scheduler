package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Admin{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long uid;

    public Admin(){}

    public void setId(long id){
        this.id = id;
    }    

    public void setUid(long uid){
        this.uid = uid;
    }

    public long getId(){
        return id;
    }

    public long getUid(){
        return uid;
    }

}