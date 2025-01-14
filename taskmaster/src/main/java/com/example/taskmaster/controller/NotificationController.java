package com.example.taskmaster.controller;

import com.example.taskmaster.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/test-email")
    public ResponseEntity<String> testEmail() {
        notificationService.sendEmail("test-recipient@example.com", "Test Email", "This is a test email.");
        return ResponseEntity.ok("Test email sent successfully!");
    }

}

