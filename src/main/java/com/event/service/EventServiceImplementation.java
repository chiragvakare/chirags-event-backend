package com.event.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.event.dto.EventDto;
import com.event.exception.EventNotFoundException;
import com.event.model.Event;
import com.event.model.User;
import com.event.repository.EventRepository;
import com.event.repository.RegistrationRepository;
import com.event.repository.UserRepository;

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
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        event.getRegisteredUsers().add(user);
        user.getRegisteredEvents().add(event);

        eventRepository.save(event);
        userRepository.save(user);
    }
	
	@Override
    public Set<Event> getRegisteredEventsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getRegisteredEvents();
    }
}




