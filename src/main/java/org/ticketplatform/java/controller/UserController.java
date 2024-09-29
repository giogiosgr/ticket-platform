package org.ticketplatform.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
