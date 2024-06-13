package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;
    private User testUser;

    private final String testEmail = "test@example.com";
    private final String testPassword = "password";

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setEmail(testEmail);
        testUser.setPassword(passwordEncoder.encode(testPassword));
        testUser.setRole("USER");

        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);
        Mockito.when(userRepository.findByEmail(testEmail)).thenReturn(java.util.Optional.of(testUser));
    }

    @AfterEach
    public void teardown() {
        userRepository.delete(testUser);
    }

    @Test
    public void testCreateUser() {
        User created = userService.createUser(testUser);
        assertThat(created.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(passwordEncoder.matches(testPassword, created.getPassword())).isTrue();
    }

    @Test
    public void testGetUserByEmail() {
        User found = userService.getUserByEmail(testEmail).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(testUser.getEmail());
        User found1 = userService.getUserByEmail("test"+testEmail).orElse(null);
        assertThat(found1).isNull();
    }
}
