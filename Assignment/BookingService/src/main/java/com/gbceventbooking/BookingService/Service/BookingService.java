package com.gbceventbooking.BookingService.Service;

import com.gbceventbooking.BookingService.Model.Booking;
import com.gbceventbooking.BookingService.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${room.service.url}")
    private String roomServiceUrl;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking createBooking(Booking booking) {
        if (isRoomAvailable(booking.getRoomId(), booking.getStartDate(), booking.getEndDate())) {
            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Room is not available for the selected dates.");
        }
    }

    public Booking updateBooking(String id, Booking booking) {
        Booking existingBooking = getBookingById(id);
        if (existingBooking != null) {
            existingBooking.setRoomId(booking.getRoomId());
            existingBooking.setUserId(booking.getUserId());
            existingBooking.setStartDate(booking.getStartDate());
            existingBooking.setEndDate(booking.getEndDate());
            return bookingRepository.save(existingBooking);
        }
        return null;
    }

    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }

    // Helper method to check room availability
    private boolean isRoomAvailable(String roomId, String startDate, String endDate) {
        String url = String.format("%s/api/rooms/%s/availability?startDate=%s&endDate=%s", 
                                    roomServiceUrl, roomId, startDate, endDate);
        try {
            return restTemplate.getForObject(url, Boolean.class);
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Error checking room availability: " + e.getMessage());
        }
    }
}
