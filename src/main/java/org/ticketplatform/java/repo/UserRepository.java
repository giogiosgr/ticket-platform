package org.ticketplatform.java.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.ticketplatform.java.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	public Optional<User> findByUsername(String title);
	
}

