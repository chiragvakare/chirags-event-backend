package com.yuvraj.service;

import java.util.List;

import com.yuvraj.model.Registration;
import com.yuvraj.model.User;

public interface RegistrationService {

	Registration registerForEvent(Long id, Long userId);

	List<User> getAttendeesForEvent(Long id);
	
}
