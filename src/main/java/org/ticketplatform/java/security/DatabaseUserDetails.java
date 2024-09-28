package org.ticketplatform.java.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.ticketplatform.java.model.Role;
import org.ticketplatform.java.model.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class DatabaseUserDetails implements UserDetails {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private final Integer id;
	private final String username;
	private final String password;
	private final Set<GrantedAuthority> authorities;

	public DatabaseUserDetails(User user) {
		
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		
		authorities = new HashSet<GrantedAuthority>();
		for (Role role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return authorities;
	}
	
	@Override
	public String getPassword() {
		
		return this.password;
	}
	
	@Override
	public String getUsername() {
		
		return this.username;
	}

}

