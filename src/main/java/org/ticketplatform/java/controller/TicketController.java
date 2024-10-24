package org.ticketplatform.java.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ticketplatform.java.model.Ticket;
import org.ticketplatform.java.model.TicketStatus;
import org.ticketplatform.java.model.User;
import org.ticketplatform.java.repo.UserRepository;
import org.ticketplatform.java.service.CategoryService;
import org.ticketplatform.java.service.TicketService;
import org.ticketplatform.java.service.UserService;

import jakarta.validation.Valid;

@SuppressWarnings("unused")

@Controller
@RequestMapping("/tickets")
public class TicketController {

	@Autowired
	TicketService ticketService;

	@Autowired
	CategoryService categoryService;

	@Autowired
	UserService userService;

	// INDEX
	@GetMapping()
	public String index(@RequestParam(required = false) String sortField,
			@RequestParam(required = false) String sortDir, Authentication authentication, Model model) {

		// Consegna della lista di tickets alla index
		// Un admin vedrà tutti i ticket
		// Un operatore vedrà soltanto i ticket a lui assegnati

		// Parametri di sorting (campo e direzione ordinamento) e recupero tickets per sorting specifico
		if (sortField == null) {
			sortField = "id";
		}

		if (sortDir == null) {
			sortDir = "asc";
		}

		Sort sort = sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
		
		List<Ticket> tickets = new ArrayList<>();
		
		User loggedUser = userService.getByUsername(authentication.getName());
		
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			tickets = ticketService.getAllSorted(sort);
		} else {
			tickets = ticketService.getAllByUserSorted(sort, loggedUser);
		}

		model.addAttribute("tickets", tickets);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

		return "tickets/index";
	}

	// SEARCH
	@GetMapping("/search")
	public String search(@RequestParam String title, Authentication authentication, Model model) {

		// il principio di passaggio è lo stesso della index ma si agisce sulla lista filtrata per nome
		
		List<Ticket> tickets = new ArrayList<>();
		
		User loggedUser = userService.getByUsername(authentication.getName());
		
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			tickets = ticketService.getByTitleWithOrderByUpdatedAt(title);
		} else {
			tickets = ticketService.getByUserByTitleWithOrderByUpdatedAt(title, loggedUser);
		}

		model.addAttribute("tickets", tickets);

		return "tickets/index";
	}

	// SHOW
	@GetMapping("/show/{id}")
	public String show(@PathVariable int id, Authentication authentication, Model model) {

		// controllo che l'operatore loggato possa accedere alla risorsa, e gestione eccezione per risorsa non esistente	
		try {
			Ticket ticketToShow = ticketService.getById(id);
			User loggedUser = userService.getByUsername(authentication.getName());
			if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("OPERATOR"))
					&& !loggedUser.getTickets().contains(ticketToShow)) {
				return "pages/authError";
			}
			boolean isAdmin = false;
			if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
				isAdmin = true;
			}
			model.addAttribute("ticket", ticketToShow);	
			model.addAttribute("user", loggedUser);		
			model.addAttribute("isAdmin", isAdmin);	
		} catch (Exception e) {
			return "pages/notFoundError";
		}

		return "tickets/show";
	}

	// CREATE
	@GetMapping("/create")
	public String create(Model model) {

		// creazione nuovo ticket a cui viene settato lo status predefinito "da fare" prima della consegna al model
		Ticket newTicket = new Ticket();
	    newTicket.setStatus(TicketStatus.DA_FARE);

		model.addAttribute("ticket", newTicket);
		model.addAttribute("operators", userService.getAll());
		model.addAttribute("categories", categoryService.getAll());

		return "tickets/create";
	}

	// STORE
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("operators", userService.getAll());
			model.addAttribute("categories", categoryService.getAll());
			return "tickets/create";
		}

		ticketService.save(ticketForm);

		attributes.addFlashAttribute("successMessage", "ticket #" + ticketForm.getId() + " creato con successo");

		return "redirect:/tickets";
	}

	// EDIT
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Authentication authentication, Model model) {

		// all'operatore a cui non è assegnata la risorsa, oppure non è disponibile, viene restituita una pagina di errore
		Ticket ticketToEdit = ticketService.getById(id);
		User loggedUser = userService.getByUsername(authentication.getName());
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("OPERATOR"))
				&& (!loggedUser.getTickets().contains(ticketToEdit) || !loggedUser.isAvailable())) {
			return "pages/authError";
		}

		// i campi disponibili nel form di edit varieranno a seconda del ruolo
		boolean isAdmin = false;
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			isAdmin = true;
		}

		model.addAttribute("ticket", ticketToEdit);
		model.addAttribute("operators", userService.getAll());
		model.addAttribute("categories", categoryService.getAll());
		model.addAttribute("ticketStatuses", TicketStatus.values());
		model.addAttribute("isAdmin", isAdmin);
		
		return "tickets/edit";
	}

	// UPDATE
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Authentication authentication, Model model,
			RedirectAttributes attributes) {
		
		boolean isAdmin = false;
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			isAdmin = true;
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("operators", userService.getAll());
			model.addAttribute("categories", categoryService.getAll());
			model.addAttribute("ticketStatuses", TicketStatus.values());
			model.addAttribute("isAdmin", isAdmin);		
			return "tickets/edit";
		}

		ticketService.save(ticketForm);

		attributes.addFlashAttribute("successMessage", "ticket #" + ticketForm.getId() + " aggiornato con successo");

		return "redirect:/tickets/show/" + ticketForm.getId();
	}

	// DELETE
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes attributes) {

		ticketService.deleteById(id);

		attributes.addFlashAttribute("successMessage", "ticket #" + id + " eliminato con successo");

		return "redirect:/tickets";
	}

}
