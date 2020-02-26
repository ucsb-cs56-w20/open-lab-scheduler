package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import java.util.List;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
<<<<<<< HEAD
public interface CourseOfferingRepository extends CrudRepository<CourseOffering, Long>{
    List<CourseOffering> findAll();

    @Query(value = "SELECT DISTINCT course_id FROM course_offering", nativeQuery=true)
    public List<String> findAllUniqueCourses();
    public List<CourseOffering> findByInstructorEmail(String instructorEmail);
    List<CourseOffering> getCourseOfferings();
    @Query(value = "SELECT DISTINCT quarter FROM course_offering", nativeQuery=true)
    public List<String> findAllUniqueQuarters();
    @Query(value = "SELECT quarter FROM CourseOffering", nativeQuery=true)
    public List<String> findQuarters();
=======
public interface CourseOfferingRepository extends CrudRepository<CourseOffering, Long> {
    public List<CourseOffering> findByInstructorEmail(String instructorEmail);
    //List<CourseOffering> getCourseOfferings();
>>>>>>> ys/lr-implemented custom SQL query in RoomAvailabilityRepository
}
