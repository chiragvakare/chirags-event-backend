package com.yuvraj.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.yuvraj.dto.EventDto;
import com.yuvraj.exception.EventNotFoundException;
import com.yuvraj.model.Event;
import com.yuvraj.model.Registration;
import com.yuvraj.model.User;
import com.yuvraj.repository.EventRepository;
import com.yuvraj.repository.RegistrationRepository;
import com.yuvraj.repository.UserRepository;

@Service
public class EventServiceImplementation implements EventService {

    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RegistrationRepository registrationRepository;

//    @Override
//    public Page<Event> getAllEvents(int page, int size, String category, String location, LocalDate date) {
//        Pageable pageable = PageRequest.of(page, size);
//        return eventRepository.findAllByFilters(category, location, date, pageable);
//    }
    
    @Override
    public List<Event> getAllEventsList() {
        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id)
            .orElseThrow(() -> new EventNotFoundException("Event not found with id " + id));
    }


    @Override
    public Event createEvent(Event event, Long userId) {
    	User user = userRepository.getById(userId);
    	event.setUser(user);
        return eventRepository.save(event);
    }

//    @Override
//    public Event createEvent(Event event, String username) {
//        User user = userRepository.findByUsername(username)
//            .orElseThrow(() -> new RuntimeException("User not found"));
//        event.setUser(user);
//        return eventRepository.save(event);
//    }

    @Override
    public Event updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id " + id));

//        if (!event.getUser().getUsername().equals(username)) {
//            throw new RuntimeException("Unauthorized to update this event");
//        }

        event.setName(eventDto.getName());
        event.setDescription(eventDto.getDescription());
        event.setDate(eventDto.getDate());
        event.setLocation(eventDto.getLocation());
        event.setCategory(eventDto.getCategory());

        return eventRepository.save(event);
    }

//	@Override
//	public void deleteEvent(Long id) {
//		Optional<Event> event = eventRepository.findById(id);
//		if(event.isPresent()) {
//			eventRepository.delete(event.get());
//		}
//		else {
//			throw new EventNotFoundException("Even not found with id: "+id);
//		}
//	}
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Event not found"));
        eventRepository.delete(event);
    }

    @Override
    public List<Event> getEventsForUser(Long userId) {
        return eventRepository.findByUserId(userId);
    }

	@Override
	public void registerUserForEvent(Long userId, Long eventId) {
		// TODO Auto-generated method stub
		// Fetch the user and event from the database
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(eventId)
            .orElseThrow(() -> new RuntimeException("Event not found"));

        // Create a new registration
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);

        // Save the registration
        registrationRepository.save(registration);
	}
}




