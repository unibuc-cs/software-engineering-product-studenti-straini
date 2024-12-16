package com.example.taskmaster.controller;

import com.example.taskmaster.model.User;
import com.example.taskmaster.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam(required=false) String email) {
        User user = userService.registerUser(username, password, email);
        return ResponseEntity.ok(user);
    }
}
