package org.ticketplatform.java.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ticketplatform.java.model.Ticket;
import org.ticketplatform.java.model.TicketStatus;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
	
	List<Ticket> findByTitleContainingOrderByTitle(String title);
	
	List<Ticket> findByStatus(TicketStatus status);

}

