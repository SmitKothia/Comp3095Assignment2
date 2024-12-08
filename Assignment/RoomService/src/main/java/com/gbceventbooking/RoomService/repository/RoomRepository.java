package com.gbceventbooking.RoomService.repository;

import com.gbceventbooking.RoomService.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    // No need to override findById or deleteById as JpaRepository already provides these for Long IDs
}
