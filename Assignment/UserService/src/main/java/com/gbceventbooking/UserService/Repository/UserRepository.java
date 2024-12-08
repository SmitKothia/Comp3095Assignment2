package com.gbceventbooking.UserService.Repository;

import com.gbceventbooking.UserService.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Custom query methods (if needed)
}
