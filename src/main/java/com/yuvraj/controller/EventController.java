package com.yuvraj.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yuvraj.dto.EventDto;
import com.yuvraj.model.Event;
import com.yuvraj.model.RegistrationRequest;
import com.yuvraj.model.User;
import com.yuvraj.repository.UserRepository;
import com.yuvraj.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserRepository userRepository;
	
//	@GetMapping
//    public ResponseEntity<Page<Event>> getAllEvents(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String category,
//            @RequestParam(required = false) String location,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        Page<Event> events = eventService.getAllEvents(page, size, category, location, date);
//        return ResponseEntity.ok(events);
//    }
	@GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents(){
        List<Event> events = eventService.getAllEventsList();
        return ResponseEntity.ok(events);
    }
	
	 @GetMapping("/created/{id}")
	 public ResponseEntity<Event> getEventById(@PathVariable Long id) {
	       Event event = eventService.getEventById(id);
	       return ResponseEntity.ok(event);
	 }

	 @PostMapping("/{userId}")
	 public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event, @PathVariable Long userId) {
	     // Get the current authenticated user
	     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	     String username = authentication.getName(); // or however you get the username
//	     System.out.printf("My username........................ :"+username);

//	     User user = userRepository.findByUsername(username);
//	     System.out.println("My user :"+user+"      ");
	     
//	     event.setUser(user); // Assuming the Event entity has a setUser method
	     Event createdEvent = eventService.createEvent(event, userId);
	     return ResponseEntity.ok(createdEvent);
	 }
	 
	 @PutMapping("/{id}")
	 public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
	     Event updatedEvent = eventService.updateEvent(id, eventDto);
	     return ResponseEntity.ok(updatedEvent);
	 }

	 @DeleteMapping("/{id}")
	    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
	        eventService.deleteEvent(id);
	        return ResponseEntity.ok("Event deleted successfully.");
	    }
	 
	 @GetMapping("/user/{userId}")
	    public List<Event> getEventsForUser(@PathVariable Long userId) {
	        return eventService.getEventsForUser(userId);
	    }
	 
	 @PostMapping("/register")
	    public ResponseEntity<?> registerUserForEvent(@RequestBody RegistrationRequest registrationRequest) {
	        Long userId = registrationRequest.getUserId();
	        Long eventId = registrationRequest.getEventId();

	        eventService.registerUserForEvent(userId, eventId);

	        return ResponseEntity.ok().build();
	    }

	    @GetMapping("/user/{userId}/registered-events")
	    public ResponseEntity<Set<Event>> getRegisteredEvents(@PathVariable Long userId) {
	        Set<Event> registeredEvents = eventService.getRegisteredEventsByUserId(userId);
	        return ResponseEntity.ok(registeredEvents);
	    }
}