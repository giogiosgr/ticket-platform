package org.ticketplatform.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.ticketplatform.java.service.NoteService;
import org.ticketplatform.java.service.TicketService;

import jakarta.validation.Valid;

@SuppressWarnings("unused")

@Controller
@RequestMapping("/notes")
public class NoteController {
	
	@Autowired
	NoteService noteService;
	
	@Autowired
	TicketService ticketService;
	
	// CREATE
	@GetMapping("/create/{id}")
	public String create(@PathVariable int id, Model model) {

		Note newNote = new Note();
		newNote.setTicket(ticketService.getById(id));
		
		model.addAttribute("note", newNote);
		

		return "/notes/create";
	}

	// STORE
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("note") Note noteForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			return "/notes/create";
		}

		noteService.save(noteForm);

		attributes.addFlashAttribute("successMessage", "nota al ticket #" + noteForm.getTicket().getId() + " creata con successo");

		return "redirect:/tickets";
	}

	// EDIT
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model) {

		model.addAttribute("note", noteService.getById(id));

		return "/notes/edit";
	}

	// UPDATE
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("note") Note noteForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			return "/notes/edit";
		}

		noteService.save(noteForm);

		attributes.addFlashAttribute("successMessage", "nota al ticket #" + noteForm.getTicket().getId() + " modificata con successo");

		return "redirect:/tickets";
	}

	// DELETE
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes attributes) {
		
		Note noteToDelete = (noteService.getById(id));
		
		int ticketID = noteToDelete.getTicket().getId();

		noteService.delete(noteToDelete);

		attributes.addFlashAttribute("successMessage", "nota al ticket #" + ticketID + " eliminato con successo");

		return "redirect:/tickets";
	}

}
