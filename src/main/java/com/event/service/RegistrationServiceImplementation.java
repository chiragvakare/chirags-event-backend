package com.event.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.event.model.Event;
import com.event.model.Registration;
import com.event.model.User;
import com.event.repository.EventRepository;
import com.event.repository.RegistrationRepository;
import com.event.repository.UserRepository;

@Service
public class RegistrationServiceImplementation implements RegistrationService{
	
	@Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired 
    private RegistrationRepository registrationRepository;

	@Override
	public Registration registerForEvent(Long id, Long userId) {
		Optional<Event> eventOptional = eventRepository.findById(id);
        Optional<User> userOptional = userRepository.findById(userId);

        if (eventOptional.isPresent() && userOptional.isPresent()) {
            Registration registration = new Registration();
            registration.setEvent(eventOptional.get());
            registration.setUser(userOptional.get());
            return registrationRepository.save(registration);
        } else {
            throw new RuntimeException("Event or User not found");
        }
	}

	@Override
	public List<User> getAttendeesForEvent(Long id) {
		List<Registration> registrations = registrationRepository.findByEventId(id);
        return registrations.stream()
                            .map(Registration::getUser)
                            .collect(Collectors.toList());
	}
}
