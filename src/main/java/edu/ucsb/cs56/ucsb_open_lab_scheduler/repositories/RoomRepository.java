package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    List<Room> findById(long id);
    List<Room> findByName(string name);
}
