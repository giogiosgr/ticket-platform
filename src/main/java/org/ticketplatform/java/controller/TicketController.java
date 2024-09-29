package org.ticketplatform.java.controller;

import java.time.LocalDateTime;

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
import org.ticketplatform.java.repo.UserRepository;
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
	UserService userService;

	// INDEX
	@GetMapping()
	public String index(Authentication authentication, Model model) {

		// consegna dei dati a tickets/index
		model.addAttribute("tickets", ticketService.getAll());
		model.addAttribute("username", authentication.getName());

		return "/tickets/index";
	}

	// SEARCH
	@GetMapping("/search")
	public String search(@RequestParam String title, Model model) {

		model.addAttribute("tickets", ticketService.getByTitleWithOrderByTitle(title));

		return "/tickets/index";
	}

	// SHOW
	@GetMapping("/show/{id}")
	public String show(@PathVariable int id, Model model) {

		model.addAttribute("ticket", ticketService.getById(id));

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
		
		return "/tickets/create";
	}

	// STORE
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("operators", userService.getAll());
			return "/tickets/create";
		}

		ticketService.save(ticketForm);

		attributes.addFlashAttribute("successMessage", "ticket " + ticketForm.getId() + " creato con successo");

		return "redirect:/tickets";
	}

	// EDIT
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model) {

		model.addAttribute("ticket", ticketService.getById(id));
		model.addAttribute("operators", userService.getAll());

		return "/tickets/edit";
	}

	// UPDATE
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("ticket") Ticket ticketForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("operators", userService.getAll());
			return "/tickets/edit";
		}

		ticketService.save(ticketForm);

		attributes.addFlashAttribute("successMessage", "ticket " + ticketForm.getId() + " modificato con successo");

		return "redirect:/tickets";
	}
	
	// DELETE
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable int id, RedirectAttributes attributes) {
		
		ticketService.deleteById(id);
		
		attributes.addFlashAttribute("successMessage", "ticket #" + id + " eliminato con successo");
		
		return "redirect:/tickets";
	}

}
