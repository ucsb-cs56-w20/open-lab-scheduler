package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import java.util.List;


import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeSlotAssignmentRepository extends CrudRepository<TimeSlotAssignment, Long> {
    
    List<TimeSlotAssignment> findByTutorAndCourseOffering(Tutor tutor, CourseOffering courseOffering);
}
