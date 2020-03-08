package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;

import java.util.List;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorCheckIn;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorCheckInRepository extends CrudRepository<TutorCheckIn, Long> {
    List<TutorCheckIn> findById(TimeSlotAssignment timeSlotAssignmentId);
}