package com.yuvraj.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yuvraj.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}