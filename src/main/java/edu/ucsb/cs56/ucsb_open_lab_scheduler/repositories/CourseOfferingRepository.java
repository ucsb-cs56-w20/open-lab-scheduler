package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import java.util.List;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.CourseOffering;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseOfferingRepository extends CrudRepository<CourseOffering, Long>{
    List<CourseOffering> getCourseOfferings();
    public List<CourseOffering> findByInstructorEmail(String instructorEmail);

    @Query(value = "SELECT quarter FROM CourseOffering", nativeQuery=true)
    public List<String> findQuarters();
}
