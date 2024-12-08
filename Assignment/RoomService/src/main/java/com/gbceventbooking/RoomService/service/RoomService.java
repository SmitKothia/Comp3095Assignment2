package com.gbceventbooking.RoomService.service;

import com.gbceventbooking.RoomService.model.Room;
import com.gbceventbooking.RoomService.model.Booking;
import com.gbceventbooking.RoomService.repository.RoomRepository;
import com.gbceventbooking.RoomService.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room roomDetails) {
        return roomRepository.findById(id).map(room -> {
            room.setName(roomDetails.getName());
            room.setCapacity(roomDetails.getCapacity());
            room.setLocation(roomDetails.getLocation());
            return roomRepository.save(room);
        }).orElseThrow(() -> new RuntimeException("Room not found with id " + id));
    }

    public void deleteRoom(Long id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
        } else {
            throw new RuntimeException("Room not found with id " + id);
        }
    }

    public boolean isRoomAvailable(Long roomId, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Fetch all bookings for the room within the date range
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(roomId, start, end);

        // If no overlapping bookings are found, the room is available
        return overlappingBookings.isEmpty();
    }
}
