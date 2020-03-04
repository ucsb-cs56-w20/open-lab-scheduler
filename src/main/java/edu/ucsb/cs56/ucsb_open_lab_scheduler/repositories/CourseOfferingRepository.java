package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import java.util.List;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseOfferingRepository extends CrudRepository<CourseOffering, Long>{
    List<CourseOffering> findAll();

    @Query(value = "SELECT DISTINCT course_id FROM course_offering", nativeQuery=true)
    public List<String> findAllUniqueCourses();

    public List<CourseOffering> findByInstructorEmail(String instructorEmail);

    @Query(value = "SELECT DISTINCT quarter FROM course_offering", nativeQuery=true)
    public List<String> findAllUniqueQuarters();
}
