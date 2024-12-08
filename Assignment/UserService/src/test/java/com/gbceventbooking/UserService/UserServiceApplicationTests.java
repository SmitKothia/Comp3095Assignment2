package com.gbceventbooking.UserService;

import com.gbceventbooking.UserService.Model.User;
import com.gbceventbooking.UserService.Repository.UserRepository;
import com.gbceventbooking.UserService.Service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;

@SpringBootTest
class UserServiceApplicationTests {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void createUserTest() {
        User user = new User("1", "John Doe", "johndoe@example.com",  "ADMIN");
        when(userRepository.save(user)).thenReturn(user);
        User createdUser = userService.createUser(user);
        assertThat(createdUser).isNotNull();
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void getUserByIdTest() {
        User user = new User("1", "John Smith", "johnsmith@example.com", "USER");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        Optional<User> foundUser = userService.getUserById("1");
        assertThat(foundUser).isPresent().contains(user);
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void updateUserTest() {
        User user = new User("1", "John Smith", "johnsmith@example.com", "USER");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        User updatedUser = userService.updateUser("1", user);
        assertThat(updatedUser).isNotNull().isEqualTo(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteUserTest() {
        User user = new User("1", "John Smith", "johnsmith@example.com", "USER");
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        userService.deleteUser("1");
        verify(userRepository, times(1)).deleteById("1");
    }
}
