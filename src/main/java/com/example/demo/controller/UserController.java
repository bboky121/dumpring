package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }


    @GetMapping("/search")
    public Object searchUsers(@RequestParam(required = false) String email,
                              @RequestParam(required = false) String domain) {
        if (email != null) {
            return userService.getUserByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        } else if (domain != null) {
            return userService.getUsersByDomain(domain);
        } else {
            throw new IllegalArgumentException("Either email or domain must be provided");
        }
    }

//    public Optional<User> getUserByEmail(@RequestParam String email) {
//        return userService.getUserByEmail(email);
//    }
//
//    public List<User> getUsersByDomain(@RequestParam String domain) {
//        return userService.getUsersByDomain(domain);
//    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
        return userService.createUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id,user);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
