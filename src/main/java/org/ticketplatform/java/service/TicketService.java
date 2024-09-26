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

	public List<Ticket> getByNameWithOrderByName(String name) {

		return repo.findByNameContainingOrderByName(name);

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

}
