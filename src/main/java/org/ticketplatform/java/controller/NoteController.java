package org.ticketplatform.java.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ticketplatform.java.model.Note;
import org.ticketplatform.java.model.Ticket;
import org.ticketplatform.java.model.User;
import org.ticketplatform.java.repo.UserRepository;
import org.ticketplatform.java.service.NoteService;
import org.ticketplatform.java.service.TicketService;
import org.ticketplatform.java.service.UserService;

import jakarta.validation.Valid;

@SuppressWarnings("unused")

@Controller
@RequestMapping("/notes")
public class NoteController {

	@Autowired
	NoteService noteService;

	@Autowired
	TicketService ticketService;

	@Autowired
	UserService userService;

	// CREATE
	@GetMapping("/create/{id}")
	public String create(@PathVariable int id, Authentication authentication, Model model) {

		// all'operatore a cui non è assegnata la risorsa viene restituita una pagina di errore
		
		Ticket ticketById = ticketService.getById(id);
		if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("OPERATOR")) &&
			!userService.getByUsername(authentication.getName()).getTickets().contains(ticketById)) {		
			return "pages/authError";
		}
		
		// definizione di ticket e user a cui appartiene la nota da passare al model
		
		Note newNote = new Note();
		newNote.setTicket(ticketById);
		newNote.setUser(userService.getByUsername(authentication.getName()));

		model.addAttribute("note", newNote);

		return "notes/create";
	}

	// STORE
	@PostMapping("create")
	public String store(@Valid @ModelAttribute("note") Note noteForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			return "notes/create";
		}

		noteService.save(noteForm);
		int ticketID = noteForm.getTicket().getId();
		// update dell'orario di ultima modifica del ticket a cui appartiene la nota creata
		ticketService.update(ticketService.getById(ticketID));

		attributes.addFlashAttribute("successMessage", "nota al ticket #" + ticketID + " creata con successo");

		return ("redirect:/tickets/show/" + ticketID);
	}

	// EDIT
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Authentication authentication, Model model) {
		
		// allo user a cui non è assegnata la risorsa viene restituita una pagina di errore
		
		Note noteToEdit = noteService.getById(id);
		if (!userService.getByUsername(authentication.getName()).getNotes().contains(noteToEdit)) {		
			return "pages/authError";
		}

		model.addAttribute("note", noteService.getById(id));

		return "notes/edit";
	}

	// UPDATE
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("note") Note noteForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			return "notes/edit";
		}

		noteService.save(noteForm);
		int ticketID = noteForm.getTicket().getId();
		// update dell'orario di ultima modifica del ticket a cui appartiene la nota aggiornata
		ticketService.update(ticketService.getById(ticketID));

		attributes.addFlashAttribute("successMessage", "nota al ticket #" + ticketID + " aggiornata con successo");

		return ("redirect:/tickets/show/" + ticketID);
	}

	// DELETE
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable int id, Authentication authentication, RedirectAttributes attributes) {
		
		Note noteToDelete = noteService.getById(id);
		int ticketID = noteToDelete.getTicket().getId();
		
		noteService.delete(noteToDelete);
		// update dell'orario di ultima modifica del ticket a cui appartiene la nota eliminata
		ticketService.update(ticketService.getById(ticketID));

		attributes.addFlashAttribute("successMessage", "nota al ticket #" + ticketID + " eliminata con successo");

		return ("redirect:/tickets/show/" + ticketID);
	}

}
