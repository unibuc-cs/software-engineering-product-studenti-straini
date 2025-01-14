package com.example.taskmaster.controller;

import com.example.taskmaster.model.User;
import com.example.taskmaster.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}/settings")
    public ResponseEntity<String> updateSettings(@PathVariable Long userId,
                                                 @RequestBody Map<String, Object> settings) {
        String theme = (String) settings.get("theme");
        boolean notificationsEnabled = Boolean.parseBoolean(settings.get("notificationsEnabled").toString());
        userService.updateUserSettings(userId, theme, notificationsEnabled);
        return ResponseEntity.ok("User settings updated successfully");
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        User user = userService.findUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user);
    }
    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.findByUsername(userDetails.getUsername()).orElseThrow());
    }

    @PutMapping("/profile")
    public ResponseEntity<User> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(required = false) Boolean emailNotificationsEnabled) {
        User user = userService.findByUsername(userDetails.getUsername()).orElseThrow();

        if (email != null) {
            user.setEmail(email);
        }
        if (emailNotificationsEnabled != null) {
            user.setEmailNotificationsEnabled(emailNotificationsEnabled);
        }

        User updatedUser = userService.save(user);
        return ResponseEntity.ok(updatedUser);
    }
}