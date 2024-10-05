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

		model.addAttribute("categories", categoryService.getAll());

		return "/categories/index";
	}

	// CREATE
	@GetMapping("/create")
	public String create(Model model) {

		model.addAttribute("category", new Category());

		return "/categories/create";
	}

	// STORE
	@PostMapping("/create")
	public String store(@Valid @ModelAttribute("category") Category categoryForm, BindingResult bindingResult,
			Model model, RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			return "/categories/create";
		}

		// gestione eccezione del caso di nome già appartenente ad una categoria
		try {
			categoryService.createOrUpdateUser(categoryForm);
			attributes.addFlashAttribute("successMessage",
					"categoria '" + categoryForm.getName() + "' creata con successo");
			return "redirect:/categories";
		} catch (Exception e) {
			attributes.addFlashAttribute("notSuccessMessage", e.getMessage());
			return "redirect:/categories/create";
		}

	}

	// EDIT
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable int id, Model model) {

		model.addAttribute("category", categoryService.getById(id));

		return "/categories/edit";
	}

	// UPDATE
	@PostMapping("/edit/{id}")
	public String update(@Valid @ModelAttribute("category") Category categoryForm, BindingResult bindingResult,
			@PathVariable int id, Model model, RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			return "/categories/edit";
		}

		// gestione eccezione del caso di nome già appartenente ad una categoria
		try {
			categoryService.createOrUpdateUser(categoryForm);
			attributes.addFlashAttribute("successMessage",
					"categoria '" + categoryForm.getName() + "' modificata con successo");
			return "redirect:/categories";
		} catch (Exception e) {
			attributes.addFlashAttribute("notSuccessMessage", e.getMessage());
			return "redirect:/categories/edit/" + id;
		}
	}

	// DELETE
	@PostMapping("/delete/{id}")
	public String delete(@PathVariable int id, Authentication authentication, RedirectAttributes attributes) {

		Category categoryToDelete = categoryService.getById(id);

		categoryService.delete(categoryToDelete);

		attributes.addFlashAttribute("successMessage",
				"categoria '" + categoryToDelete.getName() + "' eliminata con successo");

		return "redirect:/categories";
	}

}
