package com.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.event.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}