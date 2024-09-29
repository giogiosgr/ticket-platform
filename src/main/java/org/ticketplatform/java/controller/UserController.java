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
		
		// Si recupera lo user loggato per switchare lo status, se i vincoli son rispettati
		User userToUpdate = userService.getByUsername(authentication.getName());
		userToUpdate.setStatus(!userToUpdate.isStatus());
		
		userService.save(userToUpdate);
		
		return "redirect:/users/show";
	}

}
