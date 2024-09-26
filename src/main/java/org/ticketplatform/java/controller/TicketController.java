package org.ticketplatform.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.ticketplatform.java.service.TicketService;


@SuppressWarnings("unused")

@Controller
@RequestMapping("/tickets")
public class TicketController {
	
	@Autowired
	TicketService ticketService;
	
	@GetMapping()
	public String index(Model model) {

		// consegna dei dati a tickets/index
		model.addAttribute("tickets", ticketService.getAll());
        
		return "/tickets/index";
	}
	
	@GetMapping("/search")
	public String search(@RequestParam String title, Model model) {

		// consegna al model di specifiche ennuple di pizza tramite JPA Query Methods (tramite service)
		model.addAttribute("tickets", ticketService.getByTitleWithOrderByTitle(title));

		return "/tickets/index";
	}

}
