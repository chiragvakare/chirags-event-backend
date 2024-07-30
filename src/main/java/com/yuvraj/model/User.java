package com.yuvraj.model;

import java.util.*;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private Collection<? extends GrantedAuthority> authorities;
    
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Event> events;
    
    @ManyToMany(mappedBy="registeredUsers")
    @JsonManagedReference
    private Set<Event> registeredEvents;

	// Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}	
	
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

	public Set<Event> getRegisteredEvents() {
		return registeredEvents;
	}

	public void setRegisteredEvents(Set<Event> registeredEvents) {
		this.registeredEvents = registeredEvents;
	}
	
	
    
}