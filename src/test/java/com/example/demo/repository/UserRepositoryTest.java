package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestUtils testUtils;

    @Test
    public void testFindByEmail() {
        User user = testUtils.createTestUser("bboky121@gmail.com", "password", "bboky", "USER");
        User foundUser = userRepository.findByEmail("testtesttest@gmail.com").orElse(null);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }
}
