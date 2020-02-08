package edu.ucsb.cs56.ucsb_open_lab_scheduler.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.ucsb.cs56.ucsb_open_lab_scheduler.entities.Admin;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Long> {
  Admin findByEmail(String email);
}