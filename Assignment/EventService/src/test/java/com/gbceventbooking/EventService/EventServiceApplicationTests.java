package com.gbceventbooking.EventService;

import com.gbceventbooking.EventService.Model.Event;
import com.gbceventbooking.EventService.Repository.EventRepository;
import com.gbceventbooking.EventService.Service.EventService;
import com.gbceventbooking.EventService.DTO.UserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class EventServiceApplicationTests {

    @Autowired
    private EventService eventService;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private RestTemplate restTemplate; // Mock RestTemplate for role validation

    @Test
    void createEventTest() {
        Event event = new Event("1", "Tech Conference", "Exciting tech event", "2024-11-15", "New York", "validOrganizerId");

        // Simulate UserDetails response from UserService
        UserDetails userDetails = new UserDetails();
        userDetails.setId("validOrganizerId");
        userDetails.setRole("STAFF");

        when(restTemplate.getForObject("http://user-service:8081/api/users/validOrganizerId", UserDetails.class)).thenReturn(userDetails);
        when(eventRepository.save(event)).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);
        assertThat(createdEvent).isNotNull();
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void getEventByIdTest() {
        Event event = new Event("1", "Tech Conference", "Exciting tech event", "2024-11-15", "New York", "validOrganizerId");
        when(eventRepository.findById("1")).thenReturn(Optional.of(event));

        Optional<Event> foundEvent = eventService.getEventById("1");
        assertThat(foundEvent).isPresent().contains(event);
        verify(eventRepository, times(1)).findById("1");
    }

    @Test
    void updateEventTest() {
        Event event = new Event("1", "Tech Conference", "Exciting tech event", "2024-11-15", "New York", "validOrganizerId");
        Event updatedEvent = new Event("1", "Updated Conference", "Updated description", "2024-11-20", "Los Angeles", "validOrganizerId");

        when(eventRepository.existsById("1")).thenReturn(true);
        when(eventRepository.save(updatedEvent)).thenReturn(updatedEvent);

        Event result = eventService.updateEvent("1", updatedEvent);
        assertThat(result).isNotNull().isEqualTo(updatedEvent);
        verify(eventRepository, times(1)).save(updatedEvent);
    }

    @Test
    void deleteEventTest() {
        when(eventRepository.existsById("1")).thenReturn(true);
        doNothing().when(eventRepository).deleteById("1");

        eventService.deleteEvent("1");
        verify(eventRepository, times(1)).deleteById("1");
    }
}
