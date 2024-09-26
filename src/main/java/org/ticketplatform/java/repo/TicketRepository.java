package org.ticketplatform.java.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ticketplatform.java.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
	List<Ticket> findByNameContainingOrderByName(String name);

}

