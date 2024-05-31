package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"bboky121\", \"email\": \"bboky121@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");

        when(userService.getUserByEmail("bboky121@gmail.com")).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/search")
                        .param("email", "bboky121@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }
}
