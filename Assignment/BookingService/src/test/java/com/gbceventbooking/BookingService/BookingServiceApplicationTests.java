package com.gbceventbooking.BookingService;

import com.gbceventbooking.BookingService.Model.Booking;
import com.gbceventbooking.BookingService.Repository.BookingRepository;
import com.gbceventbooking.BookingService.Service.BookingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceApplicationTests {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBookingTest() {
        Booking booking = new Booking("1", "Room1", "User1", "2024-11-20", "2024-11-21");

        // Mocking the room availability check to return true
        when(restTemplate.getForObject(anyString(), eq(Boolean.class))).thenReturn(true);

        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking createdBooking = bookingService.createBooking(booking);
        assertThat(createdBooking).isNotNull();
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void getBookingByIdTest() {
        Booking booking = new Booking("1", "Room1", "User1", "2024-11-20", "2024-11-21");

        when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));
        Optional<Booking> foundBooking = Optional.ofNullable(bookingService.getBookingById("1"));

        assertThat(foundBooking).isPresent().contains(booking);
        verify(bookingRepository, times(1)).findById("1");
    }

    @Test
    void updateBookingTest() {
        Booking booking = new Booking("1", "Room1", "User1", "2024-11-20", "2024-11-21");

        when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking updatedBooking = bookingService.updateBooking("1", booking);
        assertThat(updatedBooking).isNotNull().isEqualTo(booking);
        verify(bookingRepository, times(1)).save(booking);
    }

    @Test
    void deleteBookingTest() {
        Booking booking = new Booking("1", "Room1", "User1", "2024-11-20", "2024-11-21");

        when(bookingRepository.findById("1")).thenReturn(Optional.of(booking));
        bookingService.deleteBooking("1");

        verify(bookingRepository, times(1)).deleteById("1");
    }
}
