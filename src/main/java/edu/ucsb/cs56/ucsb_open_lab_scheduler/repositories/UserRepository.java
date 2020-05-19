package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.AppUser;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {
  public List<AppUser> findByEmail(String email);
}