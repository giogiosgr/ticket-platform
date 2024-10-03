package org.ticketplatform.java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ticketplatform.java.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
}
