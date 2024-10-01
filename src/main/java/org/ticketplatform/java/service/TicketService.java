package org.ticketplatform.java.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticketplatform.java.model.Ticket;
import org.ticketplatform.java.repo.TicketRepository;

import jakarta.validation.Valid;

@Service
public class TicketService {
	
	@Autowired
	private TicketRepository repo;
	
	public Ticket getById(Integer id) {

		return repo.findById(id).get();

	}
	
	public Optional<Ticket> getOptionalById(Integer id) {

		return repo.findById(id);

	}

	public List<Ticket> getAll() {

		return repo.findAll();

	}

	public List<Ticket> getByTitleWithOrderByTitle(String title) {

		return repo.findByTitleContainingOrderByTitle(title);

	}

	public void save(@Valid Ticket ticket) {

		repo.save(ticket);

	}
	
	public Ticket create(Ticket ticket) {
		
		return repo.save(ticket);
		
	}
	
    public Ticket update(Ticket ticket) {
		
		ticket.setUpdatedAt(LocalDateTime.now());
		return repo.save(ticket);
		
	}
	
	public void deleteById(int id) {

		repo.deleteById(id);

	}

	public void delete(Ticket ticketToDelete) {
		
		repo.delete(ticketToDelete);
		
	}
	
	public List<Ticket> getByStatus(String category) {
		
		return repo.findByStatus(category);
		
	}

}
