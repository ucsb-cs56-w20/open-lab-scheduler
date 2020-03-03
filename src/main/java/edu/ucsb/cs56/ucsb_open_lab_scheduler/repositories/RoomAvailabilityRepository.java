package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
<<<<<<< HEAD
<<<<<<< HEAD
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
=======
<<<<<<< HEAD
<<<<<<< HEAD
>>>>>>> ys/lr-implemented custom SQL query in RoomAvailabilityRepository
=======
>>>>>>> ys - added various queries in the RoomAvailibilityRepository and TutorAssignmentRepository, created TimeSlotAssignmentRepository


<<<<<<< HEAD
<<<<<<< HEAD
import java.util.List;
=======
=======
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
>>>>>>> ys/lr-implemented custom SQL query in RoomAvailabilityRepository
=======
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
>>>>>>> ys/lr-implemented custom SQL query in RoomAvailabilityRepository
>>>>>>> ys/lr-implemented custom SQL query in RoomAvailabilityRepository
=======
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
>>>>>>> ys - added various queries in the RoomAvailibilityRepository and TutorAssignmentRepository, created TimeSlotAssignmentRepository
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RoomAvailabilityRepository extends CrudRepository<RoomAvailability, Long> {


    List<RoomAvailability> findByQuarter(String quarter);
    @Query(value = "SELECT * from TimeSlotAssignment WHERE tutorId IN (SELECT tutorId from TutorAssignment WHERE courseOfferingId = id)", nativeQuery=true)
	List<TimeSlotAssignment> getTimeSlotAssignments(@Param("id") String id);
}