package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomOAuth2UserServiceTest {
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void loadUser() {
        String email = "test@testest.com";
        String name = "TestUser";
        User testUser = new User();
        testUser.setEmail(email);
        testUser.setName(name);
        testUser.setRole("USER");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(java.util.Optional.of(testUser));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(testUser);

        OAuth2UserRequest userRequest = Mockito.mock(OAuth2UserRequest.class);
        OAuth2User oAuth2User = Mockito.mock(OAuth2User.class);
        Mockito.when(oAuth2User.getAttribute("email")).thenReturn(email);
        Mockito.when(oAuth2User.getAttribute("name")).thenReturn(name);

        OAuth2User loadedUser = customOAuth2UserService.loadUser(userRequest);
        assertThat(loadedUser).isNotNull();
        assertThat(Optional.ofNullable(loadedUser.getAttribute("email"))).isEqualTo("test@example.com");
    }
}
