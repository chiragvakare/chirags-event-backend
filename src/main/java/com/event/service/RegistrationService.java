package com.event.service;

import java.util.List;

import com.event.model.Registration;
import com.event.model.User;

public interface RegistrationService {

	Registration registerForEvent(Long id, Long userId);

	List<User> getAttendeesForEvent(Long id);
	
}
