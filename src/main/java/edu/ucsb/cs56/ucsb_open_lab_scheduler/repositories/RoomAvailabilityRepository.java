package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.RoomAvailability;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomAvailabilityRepository extends CrudRepository<RoomAvailability, Long> {
    List<RoomAvailability> findByQuarter(String quarter);
    @Query(value = "SELECT * from TimeSlotAssignment WHERE tutorId IN (SELECT tutorId from TutorAssignment WHERE courseOfferingId = id)", nativeQuery=true)
	List<TimeSlotAssignment> getTimeSlotAssignments(@Param("id") String id);
}