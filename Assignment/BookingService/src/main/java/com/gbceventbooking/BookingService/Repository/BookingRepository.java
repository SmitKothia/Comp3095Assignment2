package com.gbceventbooking.BookingService.Repository;

import com.gbceventbooking.BookingService.Model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {
}