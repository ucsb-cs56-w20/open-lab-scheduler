package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TimeSlotAssignmentRepository extends CrudRepository<TimeSlotAssignment, Long> {
    
    List<TimeSlotAssignment> findByTutorAndCourseOffering(Tutor tutor, CourseOffering courseOffering);

}