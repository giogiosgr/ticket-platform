package org.ticketplatform.java.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.ticketplatform.java.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	
	boolean existsByName(String name);

}