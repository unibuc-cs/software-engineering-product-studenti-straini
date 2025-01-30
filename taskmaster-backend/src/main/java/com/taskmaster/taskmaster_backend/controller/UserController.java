package com.taskmaster.taskmaster_backend.controller;

import com.taskmaster.taskmaster_backend.model.User;
import com.taskmaster.taskmaster_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String password = userData.get("password");
        String email = userData.get("email");

        try {
            User newUser = userService.registerUser(username, password, email);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Optional<User> userOpt = userService.findByUsername(username);
        if (userOpt.isEmpty() || !userService.validatePassword(userOpt.get(), password)) {
            return ResponseEntity.status(401).body("Username sau parola incorecta!");
        }

        User user = userOpt.get();

        Map<String, Object> response = Map.of(
                "userId", user.getId(),
                "username", user.getUsername()
        );

        return ResponseEntity.ok(response);
    }

}
