package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.TimeSlotAssignment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface RoomAvailabilityRepository extends CrudRepository<RoomAvailability, Long> {
    @Query(value = "SELECT * from TimeSlotAssignment WHERE tutorId IN (SELECT tutorId from TutorAssignment WHERE courseOfferingId = id)", nativeQuery=true)
	List<TimeSlotAssignment> getTimeSlotAssignments(@Param("id") String id);
}