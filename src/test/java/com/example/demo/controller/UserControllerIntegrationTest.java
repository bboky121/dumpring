package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TestUtils testUtils;

    public UserControllerIntegrationTest() {
    }

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void testUserRegistration() throws Exception {
        User user = testUtils.createTestUser("bboky121@gmail.com", "password", "USER");

        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateUser() throws Exception {
        String userJson = "{\"name\": \"bboky121\", \"email\": \"bboky121@gmail.com\", \"password\": \"password\", \"role\": \"USER\"}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void testGetUserById() throws Exception {
        User user = new User();
        user.setName("bboky121");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("bboky121@gmail.com");
        user = userRepository.save(user);

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void testGetUserByEmail() throws Exception {
        User user = new User();
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole("ADMIN");
        user = userRepository.save(user);

        mockMvc.perform(get("/users/search")
                        .param("email", "bboky121@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("bboky121"))
                .andExpect(jsonPath("$.email").value("bboky121@gmail.com"));
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void testUpdateUser() throws Exception {
        User user = new User();
        user.setName("bboky121");
        user.setEmail("bboky121@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole("ADMIN");
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
    @WithMockUser(username = "ADMIN_bboky121", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        User user = new User();
        user.setName("delete bboky");
        user.setEmail("delete_bboky@gmail.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRole("ADMIN");
        user = userRepository.save(user);

        mockMvc.perform(delete("/users/" + user.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "bboky121", roles = {"USER"})
    public void testGetUsersByDomain() throws Exception {
        User user1 = new User();
        user1.setName("bboky");
        user1.setEmail("bboky@gmail.com");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setRole("USER");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("bboky1");
        user2.setEmail("bboky1@gmail.com");
        user2.setPassword(passwordEncoder.encode("password"));
        user2.setRole("USER");
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("bboky2");
        user3.setEmail("bboky2@fmail.com");
        user3.setPassword(passwordEncoder.encode("password"));
        user3.setRole("USER");
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
