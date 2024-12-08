package com.gbceventbooking.RoomService.repository;

import com.gbceventbooking.RoomService.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.roomId = :roomId " +
           "AND ((b.startDate <= :endDate AND b.startDate >= :startDate) " +
           "OR (b.endDate >= :startDate AND b.endDate <= :endDate) " +
           "OR (b.startDate <= :startDate AND b.endDate >= :endDate))")
    List<Booking> findOverlappingBookings(@Param("roomId") Long roomId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}
