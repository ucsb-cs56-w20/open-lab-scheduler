package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TutorAssignment;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface TutorAssignmentRepository extends CrudRepository<TutorAssignment, Long> {
    List<TutorAssignment> findByCourseOffering(CourseOffering courseOffering);
    List<TutorAssignment> findByTutorAndCourseOfferingQuarter(Tutor tutor, String courseOfferingQuarter);

    @Transactional
    void deleteByTutorId(long tutorId);
}
