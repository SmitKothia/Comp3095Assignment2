package com.gbceventbooking.EventService.Service;

import com.gbceventbooking.EventService.Model.Event;
import com.gbceventbooking.EventService.Repository.EventRepository;
import com.gbceventbooking.EventService.DTO.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public Event createEvent(Event event) {
        // Validate organizer's role
        UserDetails userDetails = getUserDetails(event.getOrganizerId());
        if (!isValidRole(userDetails)) {
            logger.error("User with ID {} does not have the 'STAFF' role to organize larger events.", event.getOrganizerId());
            throw new RuntimeException("User does not have sufficient privileges to create a larger event.");
        }
        return eventRepository.save(event);
    }

    public Event updateEvent(String id, Event updatedEvent) {
        if (eventRepository.existsById(id)) {
            updatedEvent.setId(id);
            return eventRepository.save(updatedEvent);
        } else {
            throw new RuntimeException("Event not found");
        }
    }

    public void deleteEvent(String id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        } else {
            throw new RuntimeException("Event not found");
        }
    }

    private UserDetails getUserDetails(String userId) {
        String url = userServiceUrl + "/" + userId;
        try {
            return restTemplate.getForObject(url, UserDetails.class);
        } catch (HttpClientErrorException.NotFound e) {
            logger.error("User not found with ID: {}", userId);
            throw new RuntimeException("User not found with ID: " + userId, e);
        } catch (Exception e) {
            logger.error("Error while fetching user details for ID: {}", userId);
            throw new RuntimeException("Error while fetching user details for ID: " + userId, e);
        }
    }

    private boolean isValidRole(UserDetails userDetails) {
        // Only allow "STAFF" role to organize larger events
        return "STAFF".equals(userDetails.getRole());
    }
}
