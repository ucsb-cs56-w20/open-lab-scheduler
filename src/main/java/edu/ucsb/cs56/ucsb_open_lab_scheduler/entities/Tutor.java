package edu.ucsb.cs56.ucsb_open_lab_scheduler.entities;

import com.opencsv.bean.CsvBindByPosition;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
public class Tutor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CsvBindByPosition(position = 0)
    @NotBlank(message = "Email is required")
    private String email;

    @CsvBindByPosition(position = 1)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @CsvBindByPosition(position = 2)
    @NotBlank(message = "First name is required")
    private String firstName;

    private boolean isActive;

    public Tutor() {
        this.isActive = true;
    }

    public Tutor(String email, String fname, String lname){
        this.email = email;
        this.firstName = fname;
        this.lastName = lname;
        this.isActive = true;
    }

    public Tutor(String email, String fname, String lname, boolean isActive){
        this.email = email;
        this.firstName = fname;
        this.lastName = lname;
        this.isActive = isActive;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setFirstName(String fname){
        this.firstName = fname;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setLastName(String lname){
        this.lastName = lname;
    }

    public String getLastName(){
        return this.lastName;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }

    @Override
    public String toString(){
        return "{"+
            " id='"+id+"'"+
            ", email='"+email+"'"+
            ", firstName='"+firstName+"'"+
            ", lastName='"+lastName+"'"+
            ", isActive='"+isActive+"'"+
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Tutor)) {
            return false;
        }
        Tutor tutor = (Tutor) o;
        return id == tutor.id && Objects.equals(email, tutor.email) && Objects.equals(firstName, tutor.firstName) && Objects.equals(lastName, tutor.lastName) && isActive == tutor.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, isActive);
    }
}
