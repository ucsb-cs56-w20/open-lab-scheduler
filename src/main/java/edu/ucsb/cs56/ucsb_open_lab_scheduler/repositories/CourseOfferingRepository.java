package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import java.util.List;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CourseOfferingRepository extends CrudRepository<CourseOffering, Long>{
    public CourseOffering findByQuarterAndCourseId(String quarter, String course_id);
    List<CourseOffering> findAll();

    @Query(value = "SELECT DISTINCT course_id FROM course_offering", nativeQuery=true)
    public List<String> findAllUniqueCourses();

    @Query(value = "SELECT DISTINCT quarter FROM course_offering", nativeQuery=true)
    public List<String> findAllUniqueQuarters();

    public List<CourseOffering> findByInstructorEmail(String instructorEmail);

    public List<CourseOffering> findByQuarter(String quarter);
}
