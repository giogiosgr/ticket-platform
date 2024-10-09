package org.ticketplatform.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticketplatform.java.model.Category;
import org.ticketplatform.java.repo.CategoryRepository;

import jakarta.validation.Valid;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repo;
	
	public Category getById(Integer id) {

		return repo.findById(id).get();

	}
	
	public Optional<Category> getOptionalById(Integer id) {

		return repo.findById(id);

	}

	public List<Category> getAll() {

		return repo.findAll();

	}

	public void save(@Valid Category ticket) {

		repo.save(ticket);

	}
	
	public Category create(Category ticket) {
		
		return repo.save(ticket);
		
	}
	
	public void deleteById(int id) {

		repo.deleteById(id);

	}

	public void delete(Category categoryToDelete) {
		
		repo.delete(categoryToDelete);
		
	}

	public Category createOrUpdateCategory(Category category) throws Exception {
		
	    String name = category.getName();
		
		if (repo.existsByName(name)) {
			throw new Exception("la categoria '" + name + "' è già esistente");
		}
		
		return repo.save(category);

	}

}
