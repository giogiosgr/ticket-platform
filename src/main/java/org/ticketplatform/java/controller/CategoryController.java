package org.ticketplatform.java.controller;

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
import org.ticketplatform.java.service.CategoryService;
import org.ticketplatform.java.model.Category;

import jakarta.validation.Valid;

@SuppressWarnings("unused")

@Controller
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	CategoryService categoryService;
	
	@GetMapping()
	public String index(Model model) {

		

		return "/tickets/index";
	}


	// CREATE
	@GetMapping("/create/{id}")
	public String create(@PathVariable int id, Authentication authentication, Model model) {

	
		return "/notes/edit";
	}

	// STORE
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("note") Category noteForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

	
		return "/notes/edit";
	}

	// EDIT
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Authentication authentication, Model model) {
		
		// allo user a cui non è assegnata la risorsa viene restituita una pagina di errore
		

		return "/notes/edit";
	}

	// UPDATE
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("note") Category noteForm, BindingResult bindingResult, Model model,
			RedirectAttributes attributes) {

	

		return "redirect:/tickets/show/";
	}

	// DELETE
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable int id, Authentication authentication, RedirectAttributes attributes) {
		
	

		return "redirect:/tickets/show/";
	}

}
