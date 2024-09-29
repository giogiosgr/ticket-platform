package org.ticketplatform.java.model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.*;

@SuppressWarnings("unused")

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotNull
	@Size(max=50)
	@Column(name = "username", length=50, nullable = false, unique = true)
	private String username;
	
	@NotNull
	@Size(max=50)
	@Column(name = "password", length=50, nullable = false)
	private String password;
	
	@NotNull
	@NotEmpty
	@Size(max=100)
	@Column(name = "email", length=100, nullable = false, unique = true)
	private String email;
    
	@Column(name = "status")
	private boolean status;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Formula("(SELECT count(user.id) FROM user " 
			+ "LEFT JOIN tickets ON user.id = tickets.user_id AND tickets.status != \"completato\" "
			+ "WHERE tickets.user_id = id)")
	private Integer ongoingTickets;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role> roles;
	
	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE })
	private List<Ticket> tickets;
	
	@OneToMany(mappedBy = "user", cascade = { CascadeType.REMOVE })
	private List<Note> notes;
	
	// getters e setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Integer getOngoingTickets() {
		return ongoingTickets;
	}

	public void setOngoingTickets(Integer ongoingTickets) {
		this.ongoingTickets = ongoingTickets;
	}
	
}