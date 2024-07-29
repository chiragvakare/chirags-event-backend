package com.yuvraj.service;

import com.yuvraj.model.User;

public interface UserService {

	User registerUser(User user);
	
	User getUserByUsername(String username);
	
	boolean authenticateUser(String username, String password);
}
