package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TimeSlotAssignmentRepository extends CrudRepository<TimeSlotAssignment, Long> {
   @Query(value = "SELECT * FROM TimeSlotAssignment WHERE tutor IN (SELECT tutor from (SELECT * from TutorAssignment WHERE CourseOffering IN (SELECT CourseOffering from TutorAssignment WHERE courseOfferingId = courseId AND quarter = courseQuarter)))", nativeQuery=true)
	List<TimeSlotAssignment> getTimeSlotAssignmentsByCourseIdAndQuarter(@Param("courseId") String courseId, @Param("courseQuarter") String quarter);
}
