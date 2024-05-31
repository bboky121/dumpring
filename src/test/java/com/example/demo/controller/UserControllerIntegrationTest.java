package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {
        String userJson = "{\"name\": \"bboky121\", \"email\": \"bboky121@gmail.com\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");
        user = userRepository.save(user);

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        User user = new User();
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");
        user = userRepository.save(user);

        mockMvc.perform(get("/users/search")
                        .param("email", "bboky121@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");
        user = userRepository.save(user);

        String updateUserJson = "{\"name\": \"bboky1212\", \"email\": \"bboky1212@gmail.com\"}";


        mockMvc.perform(put("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky1212"))
                .andExpect(jsonPath("$.email").value("bboky1212@gmail.com"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setName("delete bboky");
        user.setEmail("delete_bboky@gmail.com");
        user = userRepository.save(user);

        mockMvc.perform(delete("/users/" + user.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUsersByDomain() throws Exception {
        User user1 = new User();
        user1.setName("bboky");
        user1.setEmail("bboky@gmail.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("bboky1");
        user2.setEmail("bboky1@gmail.com");
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("bboky2");
        user3.setEmail("bboky2@fmail.com");
        userRepository.save(user3);

        mockMvc.perform(get("/users/search")
                        .param("domain", "@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("bboky"))
                .andExpect(jsonPath("$[0].email").value("bboky@gmail.com"))
                .andExpect(jsonPath("$[1].name").value("bboky1"))
                .andExpect(jsonPath("$[1].email").value("bboky1@gmail.com"));
    }
}
