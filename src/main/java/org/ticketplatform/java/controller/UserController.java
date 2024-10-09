package org.ticketplatform.java.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.ticketplatform.java.model.User;
import org.ticketplatform.java.repo.RoleRepository;
import org.ticketplatform.java.model.Role;
import org.ticketplatform.java.model.Ticket;
import org.ticketplatform.java.service.UserService;

import jakarta.validation.Valid;

@SuppressWarnings("unused")

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepo;

	// SHOW
	@GetMapping("/show")
	public String show(Authentication authentication, Model model) {

		model.addAttribute("user", userService.getByUsername(authentication.getName()));

		return "/users/show";
	}

	// UPDATE (status operatore)
	@PostMapping("/edit")
	public String update(Authentication authentication, RedirectAttributes attributes) {

		// Si recupera l'operatore loggato per switchare lo status, con i seguenti vincoli:
		// 1 - non può attivare lo stato "non attivo" se ha ticket aperti assegnati
		// 2 - non può attivare lo stato "non attivo" se è l'unico operatore ancora attivo

		User userToUpdate = userService.getByUsername(authentication.getName());

		if (userToUpdate.getOngoingTickets() > 0) {
			attributes.addFlashAttribute("notSuccessMessage",
					"Non puoi passare allo stato 'non disponibile' se hai ticket non completati");
			return "redirect:/users/show";
		}

		boolean userStatus = userToUpdate.isStatus();
		int availableUsersCount = 0;
		if (userStatus) {
			for (User user : userService.getAll()) {
				if (user.isStatus()) {
					availableUsersCount++;
				}
			}
			if (availableUsersCount == 1) {
				attributes.addFlashAttribute("notSuccessMessage",
						"Non puoi passare allo stato 'non disponibile' se sei l'unico operatore disponibile");
				return "redirect:/users/show";
			}
		}

		userToUpdate.setStatus(!userStatus);

		userService.save(userToUpdate);

		return "redirect:/users/show";
	}

	// CREATE (nuovo user con ruolo operatore)
	@GetMapping("/create")
	public String create(Model model) {

		User newUser = new User();

		Set<Role> newRoles = new HashSet<>();
		newRoles.add(roleRepo.findById(2).get());

		newUser.setRoles(newRoles);
		newUser.setStatus(true);
		newUser.setEmail("temp");

		model.addAttribute("user", newUser);

		return "/users/create";
	}

	// STORE (nuovo user con ruolo operatore)
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("user") User userForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

	    if (bindingResult.hasErrors()) {
	        return "/users/create";
	 	}
	    
	    String newUsername = userForm.getUsername().toLowerCase();
	    
	    userForm.setPassword("{noop}" + userForm.getPassword());
	    userForm.setUsername(newUsername);
	    userForm.setEmail(newUsername + "@ticketplatform.com");    

	    // gestione eccezione del caso di username già appartenente ad uno user
	    try {
	        userService.createUser(userForm);
	        attributes.addFlashAttribute("successMessage", "operatore '" + newUsername + "' creato con successo");
	        return "redirect:/users/show";
	    } catch (Exception e) {
	    	attributes.addFlashAttribute("notSuccessMessage", e.getMessage());
	    	return "redirect:/users/create";
	    }
	}

}
