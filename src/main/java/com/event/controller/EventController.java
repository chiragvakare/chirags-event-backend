package com.event.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.event.dto.EventDto;
import com.event.model.Event;
import com.event.model.RegistrationRequest;
import com.event.repository.UserRepository;
import com.event.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
    private Cloudinary cloudinary;

	@PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = uploadResult.get("url").toString();
            // Save imageUrl to your database
            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }
	
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
		public ResponseEntity<Event> createEvent(
			@Valid @RequestPart("event") Event event,
			@RequestPart("file") MultipartFile file,
			@PathVariable Long userId) {

			try {
				// Upload the image to Cloudinary
				Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
				String imageUrl = uploadResult.get("url").toString();

				// Set the image URL to the event
				event.setImageUrl(imageUrl);

				// Get the current authenticated user
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				String username = authentication.getName();

				// Create the event
				Event createdEvent = eventService.createEvent(event, userId);
				return ResponseEntity.ok(createdEvent);

			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			}
		}
	 
	//  @PutMapping("/{id}")
	//  public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto ,  @RequestPart(value = "file", required = false) MultipartFile file) {
	//      Event updatedEvent = eventService.updateEvent(id, eventDto);
	//      return ResponseEntity.ok(updatedEvent);
	//  }

	@PutMapping("/{id}")
	public ResponseEntity<Event> updateEvent(
			@PathVariable Long id, 
			@RequestPart("eventDto") EventDto eventDto, 
			@RequestPart(value = "file", required = false) MultipartFile file) {
		
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