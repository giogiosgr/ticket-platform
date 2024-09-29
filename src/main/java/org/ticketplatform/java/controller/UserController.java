package org.ticketplatform.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.ticketplatform.java.model.User;
import org.ticketplatform.java.service.UserService;

@SuppressWarnings("unused")

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService userService;

	// SHOW
	@GetMapping("/show")
	public String show(Authentication authentication, Model model) {

		model.addAttribute("user", userService.getByUsername(authentication.getName()));

		return "/users/show";
	}

	// UPDATE
	@PostMapping("/edit")
	public String update(Authentication authentication, RedirectAttributes attributes) {

		// Si recupera l'operatore loggato per switchare lo status, con i seguenti vincoli:
		// 1 - non può attivare lo stato "non attivo" se ha ticket aperti assegnati
		// 2 - non può attivare lo stato "non attivo" se è l'unico operatore ancora attivo

		User userToUpdate = userService.getByUsername(authentication.getName());
		boolean userStatus = userToUpdate.isStatus();

		if (userToUpdate.getOngoingTickets() > 0) {
			attributes.addFlashAttribute("notSuccessMessage",
					"Non puoi passare allo stato 'non disponibile' se hai ticket aperti");
			return "redirect:/users/show";
		}

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

}
