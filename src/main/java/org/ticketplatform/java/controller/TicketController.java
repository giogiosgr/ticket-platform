package org.ticketplatform.java.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
	public String index(Authentication authentication, Model model) {

		// consegna della lista di tickets alla index a seconda dello user loggato (ruolo e identità).
		// Un admin vedrà tutti i ticket.
		// Un operatore vedrà soltanto i ticket a lui assegnati
		
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			model.addAttribute("tickets", ticketService.getAll());
			return "/tickets/index";
		}
		
		model.addAttribute("tickets", userService.getByUsername(authentication.getName()).getTickets());

		return "/tickets/index";
	}

	// SEARCH
	@GetMapping("/search")
	public String search(@RequestParam String title, Authentication authentication, Model model) {
		
		// il principio di passaggio è lo stesso della index ma si agisce sulla lista ordinata per nome
		
		List<Ticket> orderedTickets = ticketService.getByTitleWithOrderByTitle(title);
		
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
			model.addAttribute("tickets", orderedTickets);
			return "/tickets/index";
		}
        
		List<Ticket> ownedTickets = userService.getByUsername(authentication.getName()).getTickets();
		List<Ticket> ownedOrderedTickets = new ArrayList<>();
		for (Ticket ticket : orderedTickets) {
			if (ownedTickets.contains(ticket)) {
				ownedOrderedTickets.add(ticket);
			}
		}
		
		model.addAttribute("tickets", ownedOrderedTickets);

		return "/tickets/index";
	}

	// SHOW
	@GetMapping("/show/{id}")
	public String show(@PathVariable int id, Authentication authentication, Model model) {
		
		// all'operatore a cui non è assegnata la risorsa viene restituita una pagina di errore

		Ticket ticketToShow = ticketService.getById(id);
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("OPERATOR")) &&
				!userService.getByUsername(authentication.getName()).getTickets().contains(ticketToShow)) {		
			return "/pages/authError";
		}
		
		model.addAttribute("ticket", ticketToShow);

		return "/tickets/show";
	}

	// CREATE
	@GetMapping("/create")
	public String create(Model model) {

		// creazione nuovo ticket a cui viene settato lo status predefinito "da fare" prima della consegna al model
		Ticket newTicket = new Ticket();
		newTicket.setStatus("da fare");
		
		model.addAttribute("ticket", newTicket);
		model.addAttribute("operators", userService.getAll());
		model.addAttribute("categories", categoryService.getAll());
		
		return "/tickets/create";
	}

	// STORE
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("operators", userService.getAll());
			model.addAttribute("categories", categoryService.getAll());
			return "/tickets/create";
		}

		ticketService.save(ticketForm);

		attributes.addFlashAttribute("successMessage", "ticket " + ticketForm.getId() + " creato con successo");

		return "redirect:/tickets";
	}

	// EDIT
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Authentication authentication, Model model) {
		
		// all'operatore a cui non è assegnata la risorsa viene restituita una pagina di errore

		Ticket ticketToEdit = ticketService.getById(id);
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("OPERATOR")) &&
			!userService.getByUsername(authentication.getName()).getTickets().contains(ticketToEdit)) {		
			return "/pages/authError";
		}

		model.addAttribute("ticket", ticketToEdit);
		model.addAttribute("operators", userService.getAll());
		model.addAttribute("categories", categoryService.getAll());

		return "/tickets/edit";
	}

	// UPDATE
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("operators", userService.getAll());
			model.addAttribute("categories", categoryService.getAll());
			return "/tickets/edit";
		}

		ticketService.save(ticketForm);

		attributes.addFlashAttribute("successMessage", "ticket " + ticketForm.getId() + " aggiornato con successo");

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
