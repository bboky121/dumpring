package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals(user.getName(), foundUser.get().getName());
    }

    @Test
    public void testGetUserByEmail() {
        User user = new User();
        user.setId(1L);
        user.setName("bboky");
        user.setEmail("bboky121@gmail.com");

        when(userRepository.findByEmail("bboky121@gmail.com")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByEmail("bboky121@gmail.com");

        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }
}
