package com.example.taskmaster.service;

import com.example.taskmaster.model.User;
import com.example.taskmaster.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public User registerUser(String username, String password, String email) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);     // later to be hash
        user.setEmail(email);
        return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
