package org.ticketplatform.java.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ticketplatform.java.model.Ticket;
import org.ticketplatform.java.model.TicketStatus;
import org.ticketplatform.java.service.TicketService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@SuppressWarnings("unused")

@RestController
@CrossOrigin
@RequestMapping("/api/tickets")
public class TicketRestController {

	@Autowired
	TicketService ticketService;

	@GetMapping()
	public List<Ticket> index(@RequestParam(required = false) String title) {

		List<Ticket> result;

		if (title != null && !title.isEmpty()) {
			result = ticketService.getByTitleWithOrderByUpdatedAt(title);
		} else {
			result = ticketService.getAll();
		}

		return result;
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<Ticket> show(@PathVariable int id) {

		Optional<Ticket> ticket = ticketService.getOptionalById(id);

		if (ticket.isPresent()) {
			return new ResponseEntity<>(ticket.get(), HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/category/{category}")
	public ResponseEntity<List<Ticket>> filterByCategory(@PathVariable String category) {

		List<Ticket> listByCategory = new ArrayList<>();

		for (Ticket ticket : ticketService.getAll()) {
			if (ticket.getCategory().getName().toLowerCase().equals(category.toLowerCase())) {
				listByCategory.add(ticket);
			}
		}

		if (listByCategory.size() > 0) {
			return new ResponseEntity<>(listByCategory, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/status/{status}")
	public ResponseEntity<List<Ticket>> filterByStatus(@PathVariable TicketStatus status) {

		List<Ticket> listByStatus = ticketService.getByStatus(status);

		if (listByStatus.size() > 0) {
			return new ResponseEntity<>(listByStatus, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

}