package org.ticketplatform.java.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.ticketplatform.java.model.Ticket;
import org.ticketplatform.java.model.TicketStatus;
import org.ticketplatform.java.model.User;
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
	
	public List<Ticket> getAllSorted(Sort sort) {
		
		return repo.findAll(sort);
		
	}
	
	public List<Ticket> getAllByUserSorted(Sort sort, User user) {
		
		List<Ticket> tickets = repo.findAll(sort);
		List<Ticket> ownedTickets = new ArrayList<>();
		
		for (Ticket x : tickets) {
			if (user.getTickets().contains(x)) {
				ownedTickets.add(x);
			}
		}
		
		return ownedTickets;
		
	}

	public List<Ticket> getByTitleWithOrderByUpdatedAt(String title) {

		return repo.findByTitleContainingOrderByUpdatedAtDesc(title);

	}
	
	public List<Ticket> getByUserByTitleWithOrderByUpdatedAt(String title, User user) {
	
        List<Ticket> tickets = repo.findByTitleContainingOrderByUpdatedAtDesc(title);
		List<Ticket> ownedTickets = new ArrayList<>();
		
		for (Ticket x : tickets) {
			if (user.getTickets().contains(x)) {
				ownedTickets.add(x);
			}
		}
		
		return ownedTickets;
		
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
	
	public List<Ticket> getByStatus(TicketStatus status) {
		
		return repo.findByStatus(status);
		
	}

}
