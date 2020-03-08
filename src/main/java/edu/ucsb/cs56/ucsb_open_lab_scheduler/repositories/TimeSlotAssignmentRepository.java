package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Tutor;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;

import java.util.List;

import org.springframework.data.repository.query.Param;


@Repository
public interface TimeSlotAssignmentRepository extends CrudRepository<TimeSlotAssignment, Long> {
    List<TimeSlotAssignment> findByTutorId(long tutorId);
    List<TimeSlotAssignment> findByTutor(Tutor tutor);
    List<TimeSlotAssignment> findByCourseOffering(CourseOffering courseOffering);
    List<TimeSlotAssignment> findByTutorAndCourseOffering(Tutor tutor, CourseOffering courseOffering);
    List<TimeSlotAssignment> findByTutorIdAndCourseOffering(long tutorId, CourseOffering courseOffering);

    @Transactional
    void deleteByTimeSlotIdAndTutorIdAndCourseOfferingId(long timeSlotId, long tutorId, long courseOfferingId);
}

