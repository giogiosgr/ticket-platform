package org.ticketplatform.java.api;

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
			result = ticketService.getByTitleWithOrderByTitle(title);
		} else {
			result = ticketService.getAll();
		}

		return result;
	}

	@GetMapping("/category/{category}")
	public List<Ticket> showByCategory(@PathVariable String category) {

		return ticketService.getByCategory(category);
	}

	@GetMapping("/status/{status}")
	public List<Ticket> showByStatus(@PathVariable String status) {

		return ticketService.getByStatus(status);	
	}

}