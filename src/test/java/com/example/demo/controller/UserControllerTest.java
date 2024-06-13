package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setId(100000000L);
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");

        when(userService.getUserById(100000000L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/100000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));

        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"bboky121\", \"email\": \"bboky121@gmail.com\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
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
