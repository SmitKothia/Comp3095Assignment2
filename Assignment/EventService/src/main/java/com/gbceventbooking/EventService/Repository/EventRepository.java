package com.gbceventbooking.EventService.Repository;

import com.gbceventbooking.EventService.Model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    // Custom query methods can be added here if needed
}