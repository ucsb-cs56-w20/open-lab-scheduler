package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Instructor;

@Repository
public interface InstructorRepository extends CrudRepository<Instructor, Long> {
  public List<Instructor> findByEmail(String email);
}