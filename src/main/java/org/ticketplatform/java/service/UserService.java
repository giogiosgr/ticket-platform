package org.ticketplatform.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticketplatform.java.model.User;
import org.ticketplatform.java.repo.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;

	public User getById(Integer id) {

		return repo.findById(id).get();

	}

	public User getByUsername(String username) {

		return repo.findByUsername(username).get();

	}

	public Optional<User> getOptionalById(Integer id) {

		return repo.findById(id);

	}

	public List<User> getAll() {

		return repo.findAll();

	}

	public void save(User user) {

		repo.save(user);

	}

	public User createUser(User user) throws Exception {

		String userName = user.getUsername();
		
		if (repo.existsByUsername(userName)) {
			throw new Exception("L'utente '" + userName + "' esiste già nel Database");
		}
		
		return repo.save(user);

	}

}
